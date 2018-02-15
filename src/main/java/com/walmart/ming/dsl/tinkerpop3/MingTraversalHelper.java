/*
 * Copyright (c) 2016-current Walmart, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.walmart.ming.dsl.tinkerpop3;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.map.GraphStep;
import org.apache.tinkerpop.gremlin.structure.Vertex;

final class MingTraversalHelper
{
  private MingTraversalHelper() {
    // nop
  }

  static GraphTraversal<Vertex, Vertex> names(final GraphTraversal<Vertex, Vertex> traversal, final String... names) {
    return properties(traversal, "name", names);
  }

  static GraphTraversal<Vertex, Vertex> format(final GraphTraversal<Vertex, Vertex> traversal, final String format) {
    return properties(traversal, "format", format);
  }

  static GraphTraversal<Vertex, Vertex> properties(final GraphTraversal<Vertex, Vertex> traversal,
                                                   final String key,
                                                   final String... values)
  {
    GraphTraversal<Vertex, Vertex> t = traversal;
    if (values.length == 1) {
      t = traversal.has(key, values[0]);
    }
    else if (values.length > 1) {
      t = traversal.has(key, P.within(values));
    }
    return t;
  }

  static GraphTraversal<Vertex, Vertex> start(final MingTraversalSourceDsl source,
                                              final String label,
                                              final String... names)
  {
    return names(start(source, label), names);
  }

  static GraphTraversal<Vertex, Vertex> start(final MingTraversalSourceDsl source, final String label) {
    GraphTraversalSource clone = source.clone();
    clone.getBytecode().addStep(GraphTraversal.Symbols.V);
    GraphTraversal<Vertex, Vertex> traversal = new DefaultGraphTraversal<>(clone);
    traversal.asAdmin().addStep(new GraphStep<>(traversal.asAdmin(), Vertex.class, true));
    traversal = traversal.hasLabel(label);
    return traversal;
  }
}
