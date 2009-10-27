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

package com.google.template.soy.soytree;

import com.google.template.soy.soytree.SoyNode.SoyStatementNode;
import com.google.template.soy.soytree.SoyNode.SplitLevelTopNode;


/**
 * Node representing an 'if' statement.
 *
 * <p> Important: Do not use outside of Soy code (treat as superpackage-private).
 *
 * @author Kai Huang
 */
public class IfNode extends AbstractParentSoyNode<SoyNode>
    implements SplitLevelTopNode<SoyNode>, SoyStatementNode {


  /**
   * @param id The id for this node.
   */
  public IfNode(String id) {
    super(id);
  }


  @Override public String toSourceString() {
    StringBuilder sb = new StringBuilder();
    // Note: The first IfCondNode takes care of generating the 'if' tag.
    appendSourceStringForChildren(sb);
    sb.append("{/if}");
    return sb.toString();
  }

}
