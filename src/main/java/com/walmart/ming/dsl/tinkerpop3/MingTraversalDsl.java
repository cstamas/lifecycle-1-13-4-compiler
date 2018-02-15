/*
 * Copyright (c) 2016-current Walmart, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.walmart.ming.dsl.tinkerpop3;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.GremlinDsl;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;

@GremlinDsl(traversalSource = "com.walmart.ming.dsl.tinkerpop3.MingTraversalSourceDsl")
public interface MingTraversalDsl<S, E>
    extends GraphTraversal.Admin<S, E>
{
  // Core

  /**
   * component -> bucket
   */
  default GraphTraversal<S, Vertex> inBucket() {
    return out("bucket");
  }

  /**
   * bucket -> component
   */
  default GraphTraversal<S, Vertex> ofComponents() {
    return in("bucket");
  }

  /**
   * component -> version
   */
  default GraphTraversal<S, Vertex> versions(final String... names) {
    return names(out("version"), names);
  }

  /**
   * version -> component
   */
  default GraphTraversal<S, Vertex> ofComponent() {
    return in("version");
  }

  /**
   * version -> versionAsset
   */
  default GraphTraversal<S, Vertex> versionAssets(final String... names) {
    return names(out("versionAsset"), names);
  }

  /**
   * versionAsset -> version
   */
  default GraphTraversal<S, Vertex> ofVersion() {
    return in("versionAsset");
  }

  /**
   * Applies {@link #has(String, Object)} in smart way.
   */
  default GraphTraversal<S, Vertex> names(GraphTraversal<S, Vertex> traversal, final String... names) {
    return properties(traversal, "name", names);
  }

  /**
   * Applies {@link #has(String, Object)} in smart way.
   */
  default GraphTraversal<S, Vertex> properties(GraphTraversal<S, Vertex> traversal,
                                               final String key,
                                               final String... values)
  {
    if (values.length == 1) {
      traversal = traversal.has(key, values[0]);
    }
    else if (values.length > 1) {
      traversal = traversal.has(key, P.within(values));
    }
    return traversal;
  }

  // Maven

  /**
   * component -> version
   */
  default GraphTraversal<S, Vertex> releases(final String... names) {
    return names(out("version").hasNot("maven-baseVersion"), names);
  }

  /**
   * component -> version
   */
  default GraphTraversal<S, Vertex> snapshots(final String... names) {
    return names(out("version").has("maven-baseVersion"), names);
  }

  /**
   * version (using POM as parent) -> version (pom)
   */
  default GraphTraversal<S, Vertex> parent() {
    return out("parent");
  }

  /**
   * version (POM) -> versions (using POM as parent)
   */
  default GraphTraversal<S, Vertex> parentOf() {
    return in("parent");
  }

  /**
   * version (dependant) -> versions (dependency)
   */
  default GraphTraversal<S, Vertex> dependsOn() {
    return out("dependsOn");
  }

  /**
   * version (dependency) -> versions (dependant)
   */
  default GraphTraversal<S, Vertex> dependencyOf() {
    return in("dependsOn");
  }

  /**
   * version (pluginUser) -> version (plugin)
   */
  default GraphTraversal<S, Vertex> buildPlugin() {
    return out("buildPlugin");
  }

  /**
   * version (dependency) -> versions (dependant)
   */
  default GraphTraversal<S, Vertex> buildPluginOf() {
    return in("buildPlugin");
  }

  /**
   * version (extensionUser) -> version (extension)
   */
  default GraphTraversal<S, Vertex> buildExtension() {
    return out("buildExtension");
  }

  /**
   * version (extension) -> versions (extensionUser)
   */
  default GraphTraversal<S, Vertex> buildExtensionOf() {
    return in("buildExtension");
  }
}
