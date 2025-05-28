package com.neostain.csms.service;

public interface StatisticService {
    Object getTotalInvoicesLast30Days();

    Object getTotalRevenueLast30Days();

    Object getTodayRevenue();

    Object getTodayInvoices();

    Object getTotalMembers();

    Object getTotalVIPMembers();

    Object getTotalProducts(String storeId);

    Object getTotalEmployees(String storeId);

    Object getCanceledInvoices();

    Object getUncompletedInvoices();

    Object getCancelRequestedInvoices();
} 