package com.neostain.csms.service;

import com.neostain.csms.model.Invoice;
import com.neostain.csms.model.Paycheck;
import com.neostain.csms.model.ShiftReport;
import com.neostain.csms.model.Store;

import java.io.File;

public interface PrintingService {
    File printShiftReport(ShiftReport shiftReport);

    File printInvoice(Invoice invoice);

    File printPaycheck(Paycheck paycheck);

    File printStatisticsReport(Store store);
}
