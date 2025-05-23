package com.neostain.csms.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.*;
import com.neostain.csms.util.DialogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrintingServiceImpl implements PrintingService {
    @Override
    public File printShiftReport(ShiftReport shiftReport) {
        try {
            ServiceManager serviceManager = ServiceManager.getInstance();
            Store store = serviceManager.getManagementService().getStoreById(shiftReport.getStoreId());
            Employee employee = serviceManager.getManagementService().getEmployeeById(shiftReport.getEmployeeId());
            String shiftReportId = shiftReport.getId();

            Document document = new Document(PageSize.A4);
            String fileName = "ShiftReport_" + shiftReportId;
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("reports/" + fileName + ".pdf"));
            document.open();
            document.addAuthor("NeoStain");
            document.addCreationDate();
            document.addCreator("NeoStain");
            document.addTitle("Shift Report");
            document.addSubject("Shift Report");

            File fileFont = new File("fonts/JetBrainsMono-Bold.ttf");
            BaseFont bf = BaseFont.createFont(fileFont.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // font bold, 16
            com.itextpdf.text.Font font1 = new com.itextpdf.text.Font(bf, 14);
            com.itextpdf.text.Font font2 = new com.itextpdf.text.Font(bf, 12);
            File fileFont2 = new File("fonts/JetBrainsMono-Regular.ttf");
            BaseFont bf2 = BaseFont.createFont(fileFont2.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font font3 = new com.itextpdf.text.Font(bf2, 12);

            Paragraph prgDelimiter = new Paragraph("-------------------------------------------------------------------", font3);
            prgDelimiter.setAlignment(Element.ALIGN_CENTER);
            document.add(prgDelimiter);

            Paragraph prgCompany = new Paragraph("NeoStain\nCửa hàng " + store.getName(), font2);
            prgCompany.setAlignment(Element.ALIGN_CENTER);
            document.add(prgCompany);

            Paragraph prgTitle = new Paragraph("BÁO CÁO KẾT CA", font1);
            prgTitle.setAlignment(Element.ALIGN_CENTER);
            prgTitle.setSpacingBefore(10);
            prgTitle.setSpacingAfter(10);
            document.add(prgTitle);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateStart = sdf.format(shiftReport.getStartTime());
            String dateEnd = sdf.format(shiftReport.getEndTime());

            Paragraph prgContentMeta = new Paragraph(
                    "Mã ca làm: " + shiftReport.getId() +
                            "\nNgày bắt đầu: " + dateStart +
                            "\nNgày kết thúc: " + dateEnd +
                            "\nNhân viên: " + employee.getName() + " - " + employee.getId() +
                            "\nSố lượng giao dịch: " + shiftReport.getTransactionCount()
                    , font3
            );
            prgContentMeta.setAlignment(Element.ALIGN_LEFT);
            prgContentMeta.setIndentationLeft(30);
            document.add(prgContentMeta);
            document.add(prgDelimiter);

            Paragraph prgContentRevenue = new Paragraph(
                    "Tiền mặt: " + shiftReport.getCashRevenue() + "đ" +
                            "\nVí điện tử: " + shiftReport.getEwalletRevenue() + "đ" +
                            "\nNgân hàng: " + shiftReport.getBankRevenue() + "đ"
                    , font3
            );
            prgContentRevenue.setAlignment(Element.ALIGN_LEFT);
            prgContentRevenue.setIndentationLeft(30);
            document.add(prgContentRevenue);
            document.add(prgDelimiter);

            Paragraph prgContentTotal = new Paragraph(
                    "Tổng thành tiền: " + shiftReport.getCashRevenue()
                            .add(shiftReport.getBankRevenue())
                            .add(shiftReport.getEwalletRevenue()) +
                            "đ"
                    , font2
            );
            prgContentTotal.setAlignment(Element.ALIGN_LEFT);
            prgContentTotal.setIndentationLeft(30);
            document.add(prgContentTotal);
            document.add(prgDelimiter);

            document.close();
            writer.close();

            DialogFactory.showInfoDialog(
                    null,
                    "Kết ca hoàn tất!",
                    "Đã kết thúc ca làm: " + shiftReport.getId() + " lúc " + sdf.format(shiftReport.getEndTime()) +
                            "\ntại cửa hàng: " + store.getId() + " - " + store.getName() +
                            "\n\nBáo cáo kết ca đã được lưu vào: reports/" + fileName + ".pdf" +
                            "\n\nỨng dụng sẽ tự động đăng xuất"
            );

            File file = new File("reports/" + fileName + ".pdf");
            if (file.exists()) {
                return file;
            }
        } catch (Exception ex) {
            DialogFactory.showErrorDialog(
                    null,
                    "Lỗi",
                    "Có lỗi xảy ra khi kết ca và đăng xuất: " + ex.getMessage()
            );
            return null;
        }
        return null;
    }

    @Override
    public File printInvoice(Invoice invoice) {
        try {
            ServiceManager svc = ServiceManager.getInstance();
            Store store = svc.getManagementService().getStoreById(invoice.getStoreId());
            Employee employee = svc.getManagementService().getEmployeeById(invoice.getEmployeeId());
            Employee manager = svc.getManagementService().getEmployeeById(store.getManagerId());
            Member member = svc.getManagementService().getMemberById(invoice.getMemberId());
            String invoiceId = invoice.getId();
            List<InvoiceDetail> invoiceDetailList = svc.getSaleService().getInvoiceDetailsByInvoiceId(invoiceId);

            // (1) Tính ra kích thước trang: 80 mm → điểm (pt)
            float widthPt = Utilities.millimetersToPoints(80f);
            // (2) Chọn chiều cao lớn (vd: 10000 pt ~ 352 mm) để chắc chắn chứa được nội dung
            float heightPt = Utilities.millimetersToPoints(350f);
            // (3) Khởi tạo trang không lề
            Rectangle pageSize = new Rectangle(widthPt, heightPt);
            Document document = new Document(pageSize, 0, 0, 5, 5);

            String fileName = "Invoice_" + invoiceId;
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("reports/" + fileName + ".pdf"));

            document.open();
            document.addAuthor("NeoStain");
            // Metadata
            document.addCreationDate();
            document.addCreator("NeoStain");
            document.addTitle("Invoice");
            document.addSubject("Invoice");

            // --- font setup ---
            BaseFont bfBold = BaseFont.createFont("fonts/JetBrainsMono-Bold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont bfReg = BaseFont.createFont("fonts/JetBrainsMono-Regular.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font h1 = new Font(bfBold, 6);
            Font body = new Font(bfReg, 6);

            Paragraph prgDelimiter = new Paragraph("-------------------------------------------------------", body);
            prgDelimiter.setAlignment(Element.ALIGN_CENTER);
            document.add(prgDelimiter);

            Paragraph prgCompany = new Paragraph("NeoStain - " + store.getName(), h1);
            prgCompany.setAlignment(Element.ALIGN_CENTER);
            document.add(prgCompany);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String creationDate = sdf.format(invoice.getCreationTime());

            Paragraph prgContentMeta = new Paragraph(
                    "Mã HĐ: " + invoiceId +
                            "\nNgày tạo HĐ: " + creationDate +
                            "\nNV: " + employee.getName() + " - " + employee.getId() +
                            "\nThành viên: " + (member != null ? (member.getId() + " - " + member.getName()) : ("Khách lẻ"))
                    , body
            );
            prgContentMeta.setAlignment(Element.ALIGN_LEFT);
            prgContentMeta.setIndentationLeft(5);

            document.add(prgContentMeta);
            document.add(prgDelimiter);

            // --- chi tiết ---
            PdfPTable table = new PdfPTable(new float[]{2, 5, 5, 1, 3});
            table.setWidthPercentage(90);

            PdfPCell cellHeaderNum = new PdfPCell(new Paragraph("STT", h1));
            PdfPCell cellHeaderProductId = new PdfPCell(new Paragraph("Mã sản phẩm", h1));
            PdfPCell cellHeaderProductName = new PdfPCell(new Paragraph("Tên sản phẩm", h1));
            PdfPCell cellHeaderQuantity = new PdfPCell(new Paragraph("SL", h1));
            PdfPCell cellHeaderUnitPrice = new PdfPCell(new Paragraph("Đơn giá", h1));

            List<PdfPCell> cells = new ArrayList<>();
            cells.add(cellHeaderNum);
            cells.add(cellHeaderProductId);
            cells.add(cellHeaderProductName);
            cells.add(cellHeaderQuantity);
            cells.add(cellHeaderUnitPrice);

            cells.forEach(cell -> {
                cell.setBorderColor(BaseColor.BLACK);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            });

            int num = 1;

            for (InvoiceDetail invoiceDetail : invoiceDetailList) {
                Product product = svc.getSaleService().getProductById(invoiceDetail.getProductId());

                PdfPCell cellNum = new PdfPCell(new Paragraph(String.valueOf(num), body));
                cellNum.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellNum.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellNum);

                PdfPCell cellProductId = new PdfPCell(new Paragraph(invoiceDetail.getProductId(), body));
                cellProductId.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductId.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellProductId);

                PdfPCell cellProductName = new PdfPCell(new Paragraph((Objects.equals(invoiceDetail.getUnitPrice(), new BigDecimal(0)) ? "KM\n" + product.getName() : product.getName()), body));
                cellProductName.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cellProductName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellProductName);

                PdfPCell cellQuantity = new PdfPCell(new Paragraph(invoiceDetail.getQuantitySold() + "", body));
                cellQuantity.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellQuantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellQuantity);

                PdfPCell cellUnitPrice = new PdfPCell(new Paragraph(invoiceDetail.getUnitPrice() + "", body));
                cellUnitPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellUnitPrice.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellUnitPrice);

                num++;
            }

            PdfPCell cellNet = new PdfPCell(new Paragraph("Thành tiền", h1));
            cellNet.setColspan(4);
            cellNet.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNet.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cellNet);

            PdfPCell cellNetNum = new PdfPCell(new Paragraph(invoice.getNetAmount() + "", body));
            cellNetNum.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNetNum.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cellNetNum);

            PdfPCell cellDiscount = new PdfPCell(new Paragraph("Giảm giá", h1));
            cellDiscount.setColspan(4);
            cellDiscount.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellDiscount.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cellDiscount);

            PdfPCell cellDiscountNum = new PdfPCell(new Paragraph((invoice.getDiscount().subtract(BigDecimal.valueOf(invoice.getPointUsed() * 40L))) + "", body));
            cellDiscountNum.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellDiscountNum.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cellDiscountNum);

            PdfPCell cellDiscountByPoint = new PdfPCell(new Paragraph("Giảm giá từ điểm sử dụng", h1));
            cellDiscountByPoint.setColspan(4);
            cellDiscountByPoint.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellDiscountByPoint.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cellDiscountByPoint);

            PdfPCell cellDiscountByPointNum = new PdfPCell(new Paragraph(BigDecimal.valueOf(invoice.getPointUsed() * 40L) + "", body));
            cellDiscountByPointNum.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellDiscountByPointNum.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cellDiscountByPointNum);

            PdfPCell cellTotal = new PdfPCell(new Paragraph("Tổng cộng", h1));
            cellTotal.setColspan(4);
            cellTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTotal.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cellTotal);

            PdfPCell cellTotalNum = new PdfPCell(new Paragraph(invoice.getTotalDue() + "", body));
            cellTotalNum.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTotalNum.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cellTotalNum);

            document.add(table);
            document.add(prgDelimiter);
            Paragraph prgContentPostMeta = new Paragraph(
                    "Phương thức thanh toán: " + invoice.getPaymentId() + " - " + svc.getSaleService().getPaymentById(invoice.getPaymentId()).getName()
                    , body
            );
            prgContentPostMeta.setAlignment(Element.ALIGN_LEFT);
            prgContentPostMeta.setIndentationLeft(5);
            document.add(prgContentPostMeta);
            document.add(prgDelimiter);
            Paragraph prgContentPostMetaPoint = new Paragraph(
                    "Số điểm tích lũy: " + (member != null ? invoice.getNetAmount().divideToIntegralValue(new BigDecimal(1000)).toPlainString() + " điểm" : 0) +
                            "\nSố điểm sử dụng: " + invoice.getPointUsed() + " điểm" + (invoice.getPointUsed() < 25 ? "" : " (Đã được giảm " + invoice.getPointUsed() * 40 + " VND)")
                    , body
            );
            prgContentPostMetaPoint.setAlignment(Element.ALIGN_LEFT);
            prgContentPostMetaPoint.setIndentationLeft(5);

            document.add(prgContentPostMetaPoint);
            document.add(prgDelimiter);

            Paragraph prgContentPostNote = new Paragraph(
                    "Quý khách sẽ không được hoàn trả hàng khi rời khỏi cửa hàng." +
                            "\nVui lòng kiểm tra kĩ trước khi rời đi."
                    , body
            );
            prgContentPostNote.setAlignment(Element.ALIGN_LEFT);
            prgContentPostNote.setIndentationLeft(5);

            document.add(prgContentPostNote);
            document.add(prgDelimiter);

            Paragraph prgContact = new Paragraph(
                    "Nếu có bất kỳ thắc mắc hay khiếu nại quý khách vui lòng gửi về email của NeoStain: neostain.cskh@gmail.com" +
                            "\nHoặc liên lạc tới số điện thoại của quản lý cửa hàng: " + manager.getPhoneNumber()
                    , body
            );
            prgContact.setAlignment(Element.ALIGN_LEFT);
            prgContact.setIndentationLeft(5);

            document.add(prgContact);
            document.add(prgDelimiter);

            document.close();
            writer.close();

            File file = new File("reports/" + fileName + ".pdf");
            if (file.exists()) {
                return file;
            }
        } catch (Exception ex) {
            DialogFactory.showErrorDialog(
                    null,
                    "Lỗi",
                    "Có lỗi xảy ra khi in hóa đơn: " + ex.getMessage()
            );
            return null;
        }
        return null;
    }

    @Override
    public File printPaycheck(Paycheck paycheck) {
        return null;
    }
}
