package com.wolf.structural.facade;

import java.io.InputStream;

/**
 * <p> Description:facade pattern
 * Provide a unified interface to a set of interfaces in a subsystem. Facade defines a higher-level interface
 * that makes the subsystem easier to use
 * A common design goal is to minimize the communication and dependencies between subsystems
 * Introduce a facade to decouple the subsystem from clients and other subsystems,
 * thereby promoting subsystem independence and portability.
 * Weak coupling lets you vary the components of the subsystem without affecting its clients
 * <p/>
 * Date: 2016/7/6
 * Time: 8:59
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Compiler {

	public void compile(InputStream inputStream) {
		Scanner scanner = new Scanner();
		Token scan = scanner.scan(inputStream);
		ProgramNodeBuilder builder = new ProgramNodeBuilder();
		new Parser().parse(scan, builder);
		RISCCodeGenerator generator = new RISCCodeGenerator();
		generator.generator(builder);
		ProgramNode parseTree = builder.getRootNode();
		parseTree.traverse(generator);
	}
}
