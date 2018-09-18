package com.wolf.utils.log;

import com.wolf.utils.ArrayUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.helpers.SubstituteLoggerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.Log4jLoggerAdapter;
import org.slf4j.impl.StaticLoggerBinder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * <p> Description: 自定义 LoggerFactory 通过 此LoggerFactory 可以获取
 * <p/>
 * Date: 2016/4/26
 * Time: 9:09
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public final class CustomLoggerFactory {

	static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";

	static final String NO_STATICLOGGERBINDER_URL = CODES_PREFIX + "#StaticLoggerBinder";
	static final String MULTIPLE_BINDINGS_URL = CODES_PREFIX + "#multiple_bindings";
	static final String NULL_LF_URL = CODES_PREFIX + "#null_LF";
	static final String VERSION_MISMATCH = CODES_PREFIX + "#version_mismatch";
	static final String SUBSTITUTE_LOGGER_URL = CODES_PREFIX + "#substituteLogger";

	static final String UNSUCCESSFUL_INIT_URL = CODES_PREFIX + "#unsuccessfulInit";
	static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory could not be successfully initialized. See also "
			+ UNSUCCESSFUL_INIT_URL;

	static final int UNINITIALIZED = 0;
	static final int ONGOING_INITILIZATION = 1;
	static final int FAILED_INITILIZATION = 2;
	static final int SUCCESSFUL_INITILIZATION = 3;
	static final int NOP_FALLBACK_INITILIZATION = 4;

	static int INITIALIZATION_STATE = UNINITIALIZED;
	static SubstituteLoggerFactory TEMP_FACTORY = new SubstituteLoggerFactory();
	static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();

	/**
	 * It is LoggerFactory's responsibility to track version changes and manage
	 * the compatibility list.
	 * <p/>
	 * <p/>
	 * It is assumed that all versions in the 1.6 are mutually compatible.
	 */
	static private final String[] API_COMPATIBILITY_LIST = new String[]{"1.6"};

	// private constructor prevents instantiation
	private CustomLoggerFactory() {
	}

	/**
	 * Force LoggerFactory to consider itself uninitialized.
	 * <p/>
	 * <p/>
	 * This method is intended to be called by classes (in the same package) for
	 * testing purposes. This method is internal. It can be modified, renamed or
	 * removed at any time without notice.
	 * <p/>
	 * <p/>
	 * You are strongly discouraged from calling this method in production code.
	 */
	static void reset() {
		INITIALIZATION_STATE = UNINITIALIZED;
		TEMP_FACTORY = new SubstituteLoggerFactory();
	}

	private final static void performInitialization() {
		singleImplementationSanityCheck();
		bind();
		if (INITIALIZATION_STATE == SUCCESSFUL_INITILIZATION) {
			versionSanityCheck();

		}
	}

	private final static void bind() {
		try {
			// the next line does the binding
			StaticLoggerBinder.getSingleton();
			INITIALIZATION_STATE = SUCCESSFUL_INITILIZATION;
			emitSubstituteLoggerWarning();
		} catch (NoClassDefFoundError ncde) {
			String msg = ncde.getMessage();
			if (msg != null && msg.indexOf("org/slf4j/impl/StaticLoggerBinder") != -1) {
				INITIALIZATION_STATE = NOP_FALLBACK_INITILIZATION;
				Util
						.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
				Util.report("Defaulting to no-operation (NOP) logger implementation");
				Util.report("See " + NO_STATICLOGGERBINDER_URL
						+ " for further details.");
			} else {
				failedBinding(ncde);
				throw ncde;
			}
		} catch (NoSuchMethodError nsme) {
			String msg = nsme.getMessage();
			if (msg != null && msg.indexOf("org.slf4j.impl.StaticLoggerBinder.getSingleton()") != -1) {
				INITIALIZATION_STATE = FAILED_INITILIZATION;
				Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
				Util.report("Your binding is version 1.5.5 or earlier.");
				Util.report("Upgrade your binding to version 1.6.x. or 2.0.x");
			}
			throw nsme;
		} catch (Exception e) {
			failedBinding(e);
			throw new IllegalStateException("Unexpected initialization failure", e);
		}
	}

	static void failedBinding(Throwable t) {
		INITIALIZATION_STATE = FAILED_INITILIZATION;
		Util.report("Failed to instantiate SLF4J LoggerFactory", t);
	}

	private final static void emitSubstituteLoggerWarning() {
		List loggerNameList = TEMP_FACTORY.getLoggerNameList();
		if (loggerNameList.size() == 0) {
			return;
		}
		Util
				.report("The following loggers will not work becasue they were created");
		Util
				.report("during the default configuration phase of the underlying logging system.");
		Util.report("See also " + SUBSTITUTE_LOGGER_URL);
		for (int i = 0; i < loggerNameList.size(); i++) {
			String loggerName = (String) loggerNameList.get(i);
			Util.report(loggerName);
		}
	}

	private final static void versionSanityCheck() {
		try {
			String requested = StaticLoggerBinder.REQUESTED_API_VERSION;

			boolean match = false;
			for (int i = 0; i < API_COMPATIBILITY_LIST.length; i++) {
				if (requested.startsWith(API_COMPATIBILITY_LIST[i])) {
					match = true;
				}
			}
			if (!match) {
				Util.report("The requested version " + requested
						+ " by your slf4j binding is not compatible with "
						+ ArrayUtils.toList(API_COMPATIBILITY_LIST).toString());
				Util.report("See " + VERSION_MISMATCH + " for further details.");
			}
		} catch (NoSuchFieldError nsfe) {
			// given our large user base and SLF4J's commitment to backward
			// compatibility, we cannot cry here. Only for implementations
			// which willingly declare a REQUESTED_API_VERSION field do we
			// emit compatibility warnings.
		} catch (Throwable e) {
			// we should never reach here
			Util.report("Unexpected problem occured during version sanity check", e);
		}
	}

	// We need to use the name of the StaticLoggerBinder class, we can't reference
	// the class itseld.
	private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";

	private static void singleImplementationSanityCheck() {
		try {
			ClassLoader loggerFactoryClassLoader = LoggerFactory.class
					.getClassLoader();
			Enumeration paths;
			if (loggerFactoryClassLoader == null) {
				paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
			} else {
				paths = loggerFactoryClassLoader
						.getResources(STATIC_LOGGER_BINDER_PATH);
			}
			List implementationList = new ArrayList();
			while (paths.hasMoreElements()) {
				URL path = (URL) paths.nextElement();
				implementationList.add(path);
			}
			if (implementationList.size() > 1) {
				Util.report("Class path contains multiple SLF4J bindings.");
				for (int i = 0; i < implementationList.size(); i++) {
					Util.report("Found binding in [" + implementationList.get(i) + "]");
				}
				Util.report("See " + MULTIPLE_BINDINGS_URL + " for an explanation.");
			}
		} catch (IOException ioe) {
			Util.report("Error getting resources from path", ioe);
		}
	}

	/**
	 * Return a logger named according to the name parameter using the statically
	 * bound {@link org.slf4j.ILoggerFactory} instance.
	 *
	 * @param name The name of the logger.
	 * @return logger
	 */
	public static Logger getLogger(String name) {
		ILoggerFactory iLoggerFactory = getILoggerFactory();
		return iLoggerFactory.getLogger(name);
	}

	public static CustomLogger getCustomLogger(String name) {
		ILoggerFactory iLoggerFactory = getILoggerFactory();
		Logger logger = iLoggerFactory.getLogger(name);
		if (logger instanceof Log4jLoggerAdapter) {
			CustomLoggerImpl customLogger = new CustomLoggerImpl((Log4jLoggerAdapter) logger);

			return customLogger;
		}

		return null;
	}

	/**
	 * 获取 自定义CustomLogger
	 * <p/>
	 * <br/> Created on 2015-8-13 上午11:42:25
	 *
	 * @param clazz
	 * @return
	 * @since 4.0
	 */
	public static CustomLogger getCustomLogger(Class<?> clazz) {
		return getCustomLogger(clazz.getName());
	}


	/**
	 * Return a logger named corresponding to the class passed as parameter, using
	 * the statically bound {@link org.slf4j.ILoggerFactory} instance.
	 *
	 * @param clazz the returned logger will be named after clazz
	 * @return logger
	 */
	public static Logger getLogger(Class clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * Return the {@link org.slf4j.ILoggerFactory} instance in use.
	 * <p/>
	 * <p/>
	 * ILoggerFactory instance is bound with this class at compile time.
	 *
	 * @return the ILoggerFactory instance in use
	 */
	public static ILoggerFactory getILoggerFactory() {
		if (INITIALIZATION_STATE == UNINITIALIZED) {
			INITIALIZATION_STATE = ONGOING_INITILIZATION;
			performInitialization();

		}
		switch (INITIALIZATION_STATE) {
			case SUCCESSFUL_INITILIZATION:
				return StaticLoggerBinder.getSingleton().getLoggerFactory();
			case NOP_FALLBACK_INITILIZATION:
				return NOP_FALLBACK_FACTORY;
			case FAILED_INITILIZATION:
				throw new IllegalStateException(UNSUCCESSFUL_INIT_MSG);
			case ONGOING_INITILIZATION:
				// support re-entrant behavior.
				// See also http://bugzilla.slf4j.org/show_bug.cgi?id=106
				return TEMP_FACTORY;
		}
		throw new IllegalStateException("Unreachable code");
	}

}
