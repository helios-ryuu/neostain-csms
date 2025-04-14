package com.neostain.csms.service.api;

import com.neostain.csms.model.Cancellation;

import java.util.ArrayList;

public interface CancellationService {

    /// Lấy dữ liệu Cancellation bằng CANCELLATION_ID từ bảng CANCELLATION
    ///
    /// @param cancellationID CANCELLATION_ID hiện tại
    /// @return Một thể hiện đối tượng Cancellation
    Cancellation getCancellation(String cancellationID);

    ArrayList<Cancellation> getCancellationsList();
}
