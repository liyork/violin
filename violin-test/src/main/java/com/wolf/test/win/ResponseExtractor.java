package com.wolf.test.win;

import com.xebialabs.overthere.winrm.Namespaces;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.dom4j.XPath;
import org.jaxen.SimpleNamespaceContext;

/**
 * Description:
 * Created on 2021/9/3 5:04 PM
 *
 * @author 李超
 * @version 0.0.1
 */
enum ResponseExtractor {
    COMMAND_ID("CommandId"),
    EXIT_CODE("CommandState[@State='http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done']/rsp:ExitCode", Namespaces.NS_WIN_SHELL),
    SHELL_ID("Selector[@Name='ShellId']", Namespaces.NS_WSMAN_DMTF),
    STDOUT("Stream[@Name='stdout']"),
    STDERR("Stream[@Name='stderr']"),
    STREAM_DONE("CommandState[@State='http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done']");

    private final String expr;
    private final Namespace ns;
    private final SimpleNamespaceContext namespaceContext;

    private ResponseExtractor(String expr) {
        this(expr, Namespaces.NS_WIN_SHELL);
    }

    private ResponseExtractor(String expr, Namespace ns) {
        this.expr = expr;
        this.ns = ns;
        this.namespaceContext = new SimpleNamespaceContext();
        this.namespaceContext.addNamespace(ns.getPrefix(), ns.getURI());
    }

    public XPath getXPath() {
        String var10000 = this.ns.getPrefix();
        XPath xPath = DocumentHelper.createXPath("//" + var10000 + ":" + this.expr);
        xPath.setNamespaceContext(this.namespaceContext);
        return xPath;
    }
}

