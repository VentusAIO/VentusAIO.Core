package com.ventus.core.interfaces;

import com.ventus.core.network.Request;
import com.ventus.core.network.Response;

public interface ISender {
    Response send(Request request);
}
