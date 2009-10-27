/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.template.soy.parsepasses;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.template.soy.base.SoySyntaxException;
import com.google.template.soy.soytree.AbstractSoyNodeVisitor;
import com.google.template.soy.soytree.SoyFileNode;
import com.google.template.soy.soytree.SoyFileSetNode;
import com.google.template.soy.soytree.TemplateNode;

import java.util.Map;


/**
 * Visitor for processing overrides.
 *
 * <p> Important: Do not use outside of Soy code (treat as superpackage-private).
 *
 * <p> Overrides are only allowed in Soy V1. An override is a template with the same name as an
 * earlier template, intended to replace the earlier definition.
 * 
 * <p> {@link #exec} should be called on a full parse tree. This visitor will check that all
 * overrides are explicit. There is no return value. A {@code SoySyntaxException} is thrown if a
 * non-explicit override is found.
 *
 * <p> Precondition: All the template names in {@code TemplateNode}s must be full names (i.e. you
 * must execute {@link PrependNamespacesVisitor} before executing this visitor).
 *
 * @author Kai Huang
 */
public class CheckOverridesVisitor extends AbstractSoyNodeVisitor<Void> {


  /** Map of template name to template node for templates seen so far. */
  private Map<String, TemplateNode> templateMap;


  @Override protected void setup() {
    templateMap = Maps.newHashMap();
  }


  // -----------------------------------------------------------------------------------------------
  // Implementations for concrete classes.


  /**
   * {@inheritDoc}
   * @throws SoySyntaxException If a non-explicit override is found.
   */
  @Override protected void visitInternal(SoyFileSetNode node) {
    visitChildren(node);
  }


  /**
   * {@inheritDoc}
   * @throws SoySyntaxException If a non-explicit override is found.
   */
  @Override protected void visitInternal(SoyFileNode node) {
    visitChildren(node);
  }


  /**
   * {@inheritDoc}
   * @throws SoySyntaxException If a non-explicit override is found.
   */
  @Override protected void visitInternal(TemplateNode node) {

    String templateName = node.getTemplateName();
    // Template name should be full name (not start with '.').
    Preconditions.checkArgument(templateName.charAt(0) != '.');

    if (templateMap.containsKey(templateName)) {
      TemplateNode prevTemplate = templateMap.get(templateName);
      // If this duplicate definition is not an explicit Soy V1 override, report error.
      if (!node.isOverride()) {
        String prevTemplateFilePath =
            prevTemplate.getNearestAncestor(SoyFileNode.class).getFilePath();
        String currTemplateFilePath = node.getNearestAncestor(SoyFileNode.class).getFilePath();
        if (currTemplateFilePath.equals(prevTemplateFilePath)) {
          throw new SoySyntaxException(
              "Found two definitions for template name '" + templateName + "', both in the file " +
              currTemplateFilePath + ".");
        } else {
          throw new SoySyntaxException(
              "Found two definitions for template name '" + templateName +
              "' in two different files " + prevTemplateFilePath + " and " +
              currTemplateFilePath + ".");
        }
      }
    } else {
      templateMap.put(templateName, node);
    }
  }

}
