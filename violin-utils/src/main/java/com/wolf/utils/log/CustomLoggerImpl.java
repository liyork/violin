package com.wolf.utils.log;

import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.impl.Log4jLoggerAdapter;
import org.slf4j.spi.LocationAwareLogger;

import java.io.Serializable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/4/26
 * Time: 9:10
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class CustomLoggerImpl extends MarkerIgnoringBase implements
		LocationAwareLogger, Serializable, CustomLogger {

	/**
	 *
	 */
	private static final long serialVersionUID = -5909363887737393300L;

	final static String FQCN = CustomLoggerImpl.class.getName();

	private Log4jLoggerAdapter newAdapter;

	CustomLoggerImpl(Log4jLoggerAdapter newAdapter) {

		this.newAdapter = newAdapter;
	}

	@Override
	public boolean isTraceEnabled() {

		return newAdapter.isTraceEnabled();
	}

	@Override
	public void trace(String msg) {

		this.newAdapter.log(null, FQCN, TRACE_INT, msg, null, null);
	}

	@Override
	public void trace(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.newAdapter.log(null, FQCN, TRACE_INT, ft.getMessage(), null, null);

	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		this.newAdapter.log(null, FQCN, TRACE_INT, ft.getMessage(), null, null);
	}

	@Override
	public void trace(String format, Object... arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
		this.newAdapter.log(null, FQCN, TRACE_INT, ft.getMessage(), null, null);
	}

	@Override
	public void trace(String msg, Throwable t) {

		this.newAdapter.log(null, FQCN, TRACE_INT, msg, null, t);
	}

	@Override
	public boolean isDebugEnabled() {

		return this.newAdapter.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		this.newAdapter.log(null, FQCN, DEBUG_INT, msg, null, null);
	}

	@Override
	public void debug(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.newAdapter.log(null, FQCN, DEBUG_INT, ft.getMessage(), null, null);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		this.newAdapter.log(null, FQCN, DEBUG_INT, ft.getMessage(), null, null);
	}

	@Override
	public void debug(String format, Object... arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
		this.newAdapter.log(null, FQCN, DEBUG_INT, ft.getMessage(), null, null);
	}

	@Override
	public void debug(String msg, Throwable t) {
		this.newAdapter.log(null, FQCN, DEBUG_INT, msg, null, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return this.newAdapter.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		this.newAdapter.log(null, FQCN, INFO_INT, msg, null, null);
	}

	@Override
	public void info(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.newAdapter.log(null, FQCN, INFO_INT, ft.getMessage(), null, null);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		this.newAdapter.log(null, FQCN, INFO_INT, ft.getMessage(), null, null);
	}

	@Override
	public void info(String format, Object... arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
		this.newAdapter.log(null, FQCN, INFO_INT, ft.getMessage(), null, null);
	}

	@Override
	public void info(String msg, Throwable t) {
		this.newAdapter.log(null, FQCN, INFO_INT, msg, null, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return this.newAdapter.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		this.newAdapter.log(null, FQCN, WARN_INT, msg, null, null);
	}

	@Override
	public void warn(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.newAdapter.log(null, FQCN, WARN_INT, ft.getMessage(), null, null);
	}

	@Override
	public void warn(String format, Object... arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
		this.newAdapter.log(null, FQCN, WARN_INT, ft.getMessage(), null, null);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		this.newAdapter.log(null, FQCN, WARN_INT, ft.getMessage(), null, null);
	}

	@Override
	public void warn(String msg, Throwable t) {
		this.newAdapter.log(null, FQCN, WARN_INT, msg, null, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return this.newAdapter.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		this.newAdapter.log(null, FQCN, ERROR_INT, msg, null, null);
	}

	@Override
	public void error(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.newAdapter.log(null, FQCN, ERROR_INT, ft.getMessage(), null, null);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		this.newAdapter.log(null, FQCN, ERROR_INT, ft.getMessage(), null, null);
	}

	@Override
	public void error(String format, Object... arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
		this.newAdapter.log(null, FQCN, ERROR_INT, ft.getMessage(), null, null);
	}

	@Override
	public void error(String msg, Throwable t) {
		this.newAdapter.log(null, FQCN, ERROR_INT, msg, null, t);
	}

	@Override
	public void log(String callerFQCN, int level, String msg, Throwable t) {

		this.log(null, callerFQCN, level, msg, null, t);

	}

	@Override
	public void log(Marker marker, String fqcn, int level, String message,
					Object[] argArray, Throwable t) {
		this.newAdapter.log(marker, fqcn, level, message, argArray, t);
	}

}
