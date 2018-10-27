package com.shyfay.admin.web.output;


import lombok.Data;
import java.io.Serializable;
/**
 * @author 牟雪
 * @since 2018/10/27
 */
@Data
public class SelectOption implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String name;
}
