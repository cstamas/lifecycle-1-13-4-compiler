/*
 * Copyright (c) 2016-current Walmart, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.walmart.ming.dsl.tinkerpop3.jsr223;

import com.walmart.ming.dsl.tinkerpop3.MingTraversal;
import com.walmart.ming.dsl.tinkerpop3.MingTraversalSource;
import org.apache.tinkerpop.gremlin.jsr223.AbstractGremlinPlugin;
import org.apache.tinkerpop.gremlin.jsr223.DefaultImportCustomizer;
import org.apache.tinkerpop.gremlin.jsr223.ImportCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MingDslGremlinPlugin
    extends AbstractGremlinPlugin
{
  private static final Logger LOG = LoggerFactory.getLogger(MingDslGremlinPlugin.class);

  private static final String NAME = "ming.dsl";

  private static final MingDslGremlinPlugin INSTANCE = new MingDslGremlinPlugin();

  private MingDslGremlinPlugin() {
    super(NAME, imports());
    LOG.info("Loaded MING DSL...");
  }

  private static ImportCustomizer imports() {
    try {
      return DefaultImportCustomizer.build()
          .addClassImports(
              MingTraversal.class,
              MingTraversalSource.class
          )
          .create();
    }
    catch (Exception ex) {
      LOG.error("Could not load MING DSL imports", ex);
      throw new RuntimeException(ex);
    }
  }

  /**
   * Used by Gremlin environment to get the instance of the plugin.
   */
  public static MingDslGremlinPlugin instance() {
    return INSTANCE;
  }
}
