package com.wolf.test.jackson.base;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;

/**
 * Description:
 * Created on 2021/3/31 9:46 AM
 *
 * @author 李超
 * @version 0.0.1
 */
@Data
@JacksonXmlRootElement(localName = "CMD")
public class RootXml {

    /**
     * Valid注解作用是为了保证层级嵌套验证成功
     */
    @Valid
    @JacksonXmlProperty(localName = "eb")
    private Eb eb;

    @Data
    public static class Eb {
        /**
         * PUB头
         */
        @Valid
        private Pub pub;

        /**
         * in
         */
        @Valid
        private In in;

    }

    @Data
    @Accessors(chain = true)
    public static class Pub {
        /**
         * 交易代码
         */
        @JacksonXmlProperty(localName = "TransCode")
        private String transCode;

        /**
         * 集团CIS号
         * 客户注册时的归属编码
         */
        @JacksonXmlProperty(localName = "CIS")
        private String cis;

        /**
         * 归属银行编号
         * 客户注册时的归属单位
         */
        @JacksonXmlProperty(localName = "BankCode")
        private String bankCode;

        /**
         * 证书ID
         * 无证书客户可上送空
         */
        @JacksonXmlProperty(localName = "ID")
        private String id;

        /**
         * 交易日期
         * ERP系统产生的交易日期，格式是yyyyMMdd
         */
        @JacksonXmlProperty(localName = "TranDate")
        private String tranDate;

        /**
         * 交易时间
         * ERP系统产生的交易时间，格式如HHmmssSSS，精确到毫秒
         */
        @JacksonXmlProperty(localName = "TranTime")
        private String tranTime;

        /**
         * 指令包序列号
         * ERP系统产生的指令包序列号，一个集团永远不能重复
         */
        @JacksonXmlProperty(localName = "fSeqno")
        private String sequence;
    }

    @Data
    public static class In {

        @JacksonXmlProperty(localName = "AccNo")
        private String accountNo;

        @JacksonXmlProperty(localName = "AreaCode")
        private String areaCode;

        @JacksonXmlProperty(localName = "MinAmt")
        private String minAmount;

        @JacksonXmlProperty(localName = "MaxAmt")
        private String maxAmount;

        @JacksonXmlProperty(localName = "BeginTime")
        private String beginTime;

        @JacksonXmlProperty(localName = "EndTime")
        private String endTime;

        @JacksonXmlProperty(localName = "NextTag")
        private String nextTag;
    }
}
