package com.wolf.test.win;//package com.wolf.test.win;
//
//import com.alibaba.fastjson.util.TypeUtils;
//import com.google.common.io.BaseEncoding;
//import com.xebialabs.overthere.util.DefaultAddressPortMapper;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import static com.xebialabs.overthere.cifs.BaseCifsConnectionBuilder.*;
//import static java.lang.System.currentTimeMillis;
//import static java.nio.charset.StandardCharsets.UTF_16LE;
//
///**
// * WinRM执行器
// *
// * @author liwenheng
// */
//@Slf4j
//public class WinRmRunner implements AutoCloseable {
//
//    private WinRmClientLocal client;
//    //private JobState jobState;
//
//
//    /**
//     * 构造执行器
//     *
//     * @param param    协议参数
//     * @param jobState 执行上下文
//     * @return 执行器
//     */
//    //public static WinRmRunner build(Map<String, Object> param, JobState jobState) {
//    public static WinRmRunner build(Map<String, Object> param) {
//        WinRm protocolParam = TypeUtils.castToJavaBean(param, WinRm.class);
//        URL url = createWinRmURL(protocolParam);
//        final WinRmClientLocal client = new WinRmClientLocal(
//                protocolParam.getUserName(), protocolParam.getPassword(),
//                url, null, 5985, DefaultAddressPortMapper.INSTANCE.socketFactory()
//        );
//        client.setWinRmTimeout(DEFAULT_WINRM_TIMEOUT);
//        client.setWinRmEnvelopSize(WINRM_ENVELOP_SIZE_DEFAULT);
//        client.setWinRmLocale("zh-cn");
//        client.setHttpsCertTrustStrategy(WINRM_HTTPS_CERTIFICATE_TRUST_STRATEGY_DEFAULT);
//        client.setHttpsHostnameVerifyStrategy(WINRM_HTTPS_HOSTNAME_VERIFICATION_STRATEGY_DEFAULT);
//        client.setKerberosUseHttpSpn(WINRM_KERBEROS_USE_HTTP_SPN_DEFAULT);
//        client.setKerberosAddPortToSpn(WINRM_KERBEROS_ADD_PORT_TO_SPN_DEFAULT);
//        client.setKerberosDebug(WINRM_KERBEROS_DEBUG_DEFAULT);
//        client.setKerberosTicketCache(WINRM_KERBEROS_TICKET_CACHE_DEFAULT);
//        //client.setConnectionTimeout(Constants.CONNECT_TIMEOUT);
//        client.setConnectionTimeout(5000);
//        client.setSoTimeout(50000);
//
//        //jobState.setOutCharset(protocolParam.getEncoding());
//        WinRmRunner runner = new WinRmRunner();
//        runner.client = client;
//        //runner.jobState = jobState;
//        return runner;
//    }
//
//    private static URL createWinRmURL(WinRm params) {
//        Boolean useSsl = params.getUseSsl();
//        final String scheme = (useSsl != null && useSsl) ? "https" : "http";
//        try {
//            return new URL(scheme, params.getIp(), params.getPort(), WINRM_CONTEXT_DEFAULT);
//        } catch (MalformedURLException e) {
//            throw new IllegalArgumentException("url格式错误", e);
//        }
//    }
//
//    private static String toPsCommand(String script) {
//        StringBuilder sb = new StringBuilder("powershell -encodedcommand ");
//        // 不要用java.util.Base64！性能差
//        String encodedScript = BaseEncoding.base64().encode(script.getBytes(UTF_16LE));
//        sb.append(encodedScript);
//        return sb.toString();
//    }
//
//    /**
//     * 执行脚本
//     *
//     * @param param 参数
//     */
//    public void sh(Map<String, Object> param) {
//        WinRmPs params = TypeUtils.castToJavaBean(param, WinRmPs.class);
//        String shell = params.getShell();
//        String command = toPsCommand(shell);
//        WinRmExec execParams = new WinRmExec();
//        execParams.setCommand(command);
//        execParams.setExpectExitZero(params.getExpectExitZero());
//        execParams.setExpectedPattern(params.getExpectedPattern());
//        execParams.setUnExpectedPattern(params.getUnExpectedPattern());
//        execParams.setTimeout(params.getTimeout());
//        execParams.setUseRegexExpected(params.getUseRegexExpected());
//        execParams.setUseRegexUnExpected(params.getUseRegexUnExpected());
//        exec(execParams);
//    }
//
//    /**
//     * 执行命令
//     *
//     * @param param 参数
//     */
//    public void exec(Map<String, Object> param) {
//        WinRmExec params = TypeUtils.castToJavaBean(param, WinRmExec.class);
//        exec(params);
//    }
//
//    private void exec(WinRmExec params) {
//        String shellId = client.createShell();
//        log.debug("shellId {}", shellId);
//        final String commandId = client.executeCommand(params.getCommand());
//        log.debug("commandId  {}", commandId);
//        Integer timeout = params.getTimeout();
//        boolean checkTimeout = timeout != null && timeout > 0;
//        long maxTime = 0L;
//        if (checkTimeout) {
//            maxTime = currentTimeMillis() + timeout * 1000;
//        }
//
//        //ByteArrayOutputStream outputStream = jobState.getOutBuilder();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
//        try {
//            do {
//                //if (maxTime > 0L && maxTime < currentTimeMillis()) {
//                //    jobState.setSummary("命令执行超时，超时设置为" + timeout + "秒");
//                //    jobState.setState(ExecState.EXCEPTION);
//                //    return;
//                //}
//                TimeUnit.MILLISECONDS.sleep(30);
//                //Util.sleepUninterruptibly(30, TimeUnit.MILLISECONDS);
//            } while (client.receiveOutput(outputStream, outputStream));
//            int exitValue = client.exitValue();
//            Boolean expectExitZero = params.getExpectExitZero();
//            String expectedPattern = params.getExpectedPattern();
//            String unExpectedPattern = params.getUnExpectedPattern();
//            //this.jobState.log("命令执行结束，返回码", exitValue);
//            //SshRunner.doCheck(exitValue, expectExitZero,
//            //        expectedPattern, params.getUseRegexExpected(),
//            //        unExpectedPattern, params.getUseRegexUnExpected(),
//            //        this.jobState);
//        } catch (IOException e) {
//            throw new IllegalStateException("通讯错误", e);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void close() {
//        // do nothing
//    }
//}
