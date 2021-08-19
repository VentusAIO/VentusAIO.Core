package com.ventus.core.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Cookie implements Serializable {
    private static final long serialVersionUID = 1617175930504994288L;

    private final String name;  // NAME= ... "$Name" style is reserved
    private String value;       // value of NAME

    // Attributes encoded in the header's cookie fields.
    private String comment;     // Comment=VALUE ... describes cookie's use
    private String commentURL;  // CommentURL="http URL" ... describes cookie's use
    private boolean toDiscard;  // Discard ... discard cookie unconditionally
    private String domain;      // Domain=VALUE ... domain that sees cookie
    private long maxAge = -1;  // Max-Age=VALUE ... cookies auto-expire
    private String path;        // Path=VALUE ... URLs that see the cookie
    private String portlist;    // Port[="portlist"] ... the port cookie may be returned to
    private boolean secure;     // Secure ... e.g. use SSL
    private boolean httpOnly;   // HttpOnly ... i.e. not accessible to scripts
    private int version = 1;    // Version=1 ... RFC 2965 style

    private long whenCreated;
}
