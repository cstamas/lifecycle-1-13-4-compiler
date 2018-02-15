/*
 * Copyright (c) 2016-current Walmart, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.walmart.ming.dsl.tinkerpop3;

import org.apache.tinkerpop.gremlin.process.traversal.TraversalStrategies;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import static com.walmart.ming.dsl.tinkerpop3.MingTraversalHelper.format;
import static com.walmart.ming.dsl.tinkerpop3.MingTraversalHelper.names;
import static com.walmart.ming.dsl.tinkerpop3.MingTraversalHelper.start;
import static java.util.Objects.requireNonNull;

public class MingTraversalSourceDsl
    extends GraphTraversalSource
{
  public MingTraversalSourceDsl(final Graph graph,
                                final TraversalStrategies traversalStrategies)
  {
    super(graph, traversalStrategies);
  }

  public MingTraversalSourceDsl(final Graph graph) {
    super(graph);
  }

  // Core

  public GraphTraversal<Vertex, Vertex> buckets(String... names) {
    return start(this, "bucket", names);
  }

  public GraphTraversal<Vertex, Vertex> components(String... names) {
    return start(this, "component", names);
  }

  public GraphTraversal<Vertex, Vertex> componentsWithFormat(String format, String... names) {
    requireNonNull(format);
    return names(format(start(this, "component"), format), names);
  }

  public GraphTraversal<Vertex, Vertex> versions(String... names) {
    return start(this, "version", names);
  }

  public GraphTraversal<Vertex, Vertex> versionsWithFormat(String format, String... names) {
    return versions(names).filter(__.ofComponent().has("format", format));
  }

  public GraphTraversal<Vertex, Vertex> versionAssets(String... names) {
    return start(this, "versionAsset", names);
  }

  public GraphTraversal<Vertex, Vertex> versionAssetsWithFormat(String format, String... names) {
    return versionAssets(names).filter(__.ofVersion().ofComponent().has("format", format));
  }

  // Maven

  public GraphTraversal<Vertex, Vertex> mavenComponents(String... names) {
    return componentsWithFormat("maven", names);
  }

  public GraphTraversal<Vertex, Vertex> mavenVersions(String... names) {
    return versionsWithFormat("maven", names);
  }

  public GraphTraversal<Vertex, Vertex> mavenVersionsWithPackaging(String packaging, String... names) {
    return versionsWithFormat("maven", names).has("maven-packaging", packaging);
  }

  public GraphTraversal<Vertex, Vertex> mavenVersionAssets(String... names) {
    return versionAssetsWithFormat("maven", names);
  }

  public GraphTraversal<Vertex, Vertex> mavenChildren(String name) {
    return V().has("name", name).repeat(__.in("parent")).until(__.inE("parent").count().is(0));
  }

  public GraphTraversal<Vertex, Vertex> mavenDependencies(String name) {
    return V().has("name", name).repeat(__.in("dependsOn")).until(__.inE("dependsOn").count().is(0));
  }

  public GraphTraversal<Vertex, Vertex> mavenBuildPlugins(String name) {
    return V().has("name", name).repeat(__.in("buildPlugin")).until(__.inE("buildPlugin").count().is(0));
  }
}
