package com.xuf.www.gobang;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Xuf on 2016/1/10.
 */
@JsonObject
public class Message {
    @JsonField
    public String mMessage;
}
