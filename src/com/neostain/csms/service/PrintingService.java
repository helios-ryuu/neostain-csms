package com.neostain.csms.service;

import com.neostain.csms.model.Invoice;
import com.neostain.csms.model.ShiftReport;

import java.io.File;

public interface PrintingService {
    File printShiftReport(ShiftReport shiftReport);

    File printInvoice(Invoice invoice);
}
