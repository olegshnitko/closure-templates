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

package com.google.template.soy.tofu.internal;

import com.google.common.base.Preconditions;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.msgs.SoyMsgBundle;
import com.google.template.soy.parseinfo.SoyTemplateInfo;
import com.google.template.soy.tofu.SoyTofu;

import java.util.Map;

import javax.annotation.Nullable;


/**
 * Represents a compiled Soy file set, with a namespace prepended to templates being rendered.
 *
 * <p> Important: Do not use outside of Soy code (treat as superpackage-private).
 *
 * @author Kai Huang
 */
class NamespacedTofu implements SoyTofu {


  /** The underlying Tofu object. */
  private final BaseTofu baseTofu;

  /** The namespace of this SoyTofu object. */
  private final String namespace;


  /**
   * @param baseTofu The underlying Tofu object.
   * @param namespace The namespace for this SoyTofu object.
   */
  NamespacedTofu(BaseTofu baseTofu, String namespace) {
    Preconditions.checkNotNull(baseTofu);
    this.baseTofu = baseTofu;
    Preconditions.checkArgument(namespace != null && namespace.length() > 0);
    this.namespace = namespace;
  }


  /**
   * {@inheritDoc}
   *
   * <p> For objects of this class, the namespace is always nonempty.
   */
  @Override public String getNamespace() {
    return namespace;
  }


  @Override public SoyTofu forNamespace(@Nullable String namespace) {
    return (namespace == null) ? baseTofu : new NamespacedTofu(baseTofu, namespace);
  }


  @Override public String render(SoyTemplateInfo templateInfo, @Nullable Map<String, ?> data,
                                 @Nullable SoyMsgBundle msgBundle) {
    return render(templateInfo.getPartialName(), data, msgBundle);
  }


  @Override public String render(SoyTemplateInfo templateInfo, @Nullable SoyMapData data,
                                 @Nullable SoyMsgBundle msgBundle) {
    return render(templateInfo.getPartialName(), data, msgBundle);
  }


  /**
   * {@inheritDoc}
   *
   * @param templateName The partial name of the template to render (e.g. ".fooTemplate").
   */
  @Override public String render(String templateName, @Nullable Map<String, ?> data,
                                 @Nullable SoyMsgBundle msgBundle) {
    return render(templateName, (data == null) ? null : new SoyMapData(data), msgBundle);
  }


  /**
   * {@inheritDoc}
   *
   * @param templateName The partial name of the template to render (e.g. ".fooTemplate").
   */
  @Override public String render(String templateName, @Nullable SoyMapData data,
                                 @Nullable SoyMsgBundle msgBundle) {
    Preconditions.checkArgument(templateName.charAt(0) == '.');
    return baseTofu.render(namespace + templateName, data, msgBundle);
  }

}
