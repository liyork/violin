package com.wolf.test.win;

/**
 * Description:
 * Created on 2021/9/3 5:14 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class WinRmExec {
    private String Command;

    private Integer timeout;

    private Boolean expectExitZero;

    private String unExpectedPattern;

    private String expectedPattern;

    private String useRegexExpected;

    public String getCommand() {
        return Command;
    }

    public void setCommand(String command) {
        Command = command;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getExpectExitZero() {
        return expectExitZero;
    }

    public void setExpectExitZero(Boolean expectExitZero) {
        this.expectExitZero = expectExitZero;
    }

    public String getUnExpectedPattern() {
        return unExpectedPattern;
    }

    public void setUnExpectedPattern(String unExpectedPattern) {
        this.unExpectedPattern = unExpectedPattern;
    }

    public String getExpectedPattern() {
        return expectedPattern;
    }

    public void setExpectedPattern(String expectedPattern) {
        this.expectedPattern = expectedPattern;
    }


    public String getUseRegexExpected() {
        return useRegexExpected;
    }

    public void setUseRegexExpected(String useRegexExpected) {
        this.useRegexExpected = useRegexExpected;
    }


    public String useRegexUnExpected;

    public String getUseRegexUnExpected() {
        return useRegexUnExpected;
    }

    public void setUseRegexUnExpected(String useRegexUnExpected) {
        this.useRegexUnExpected = useRegexUnExpected;
    }
}
