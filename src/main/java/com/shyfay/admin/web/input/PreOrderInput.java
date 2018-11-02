package com.shyfay.admin.web.input;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 牟雪
 * @since 2018/11/1
 */
@Data
public class PreOrderInput implements Serializable {
    private static final long serialVersionUID = 1L;
    private String outTradeNo;
    private String subject;
    private String totalAmount;
    private String extData;
}
