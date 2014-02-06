package org.core4j.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.core4j.Enumerable;
import org.core4j.Predicates;

public class XElement extends XContainer implements XNameable {

  private XName xname;
  private final List<XAttribute> attributes = new ArrayList<XAttribute>();

  public XElement(String name, Object... content) {
    this.xname = new XName(null, name);
    for (Object obj : content) {
      add(obj);
    }
  }

  @Override
  public void add(Object content) {
    if (content instanceof XAttribute) {
      attributes.add((XAttribute) content);
    } else {
      super.add(content);
    }
  }

  @Override
  public XmlNodeType getNodeType() {
    return XmlNodeType.ELEMENT;
  }

  public Enumerable<XAttribute> attributes() {
    return Enumerable.create(attributes);
  }

  public XAttribute attribute(String name) {
    return attributes().firstOrNull(Predicates.<XAttribute> xnameEquals(name));
  }

  @Override
  protected XElement getXElement() {
    return this;
  }

  @Override
  public XName getName() {
    return xname;
  }

  @Override
  public String toString() {
    return toString(XmlFormat.NOT_INDENTED);
  }

  @Override
  public String toString(XmlFormat format) {
    boolean enableIndent = format.isIndentEnabled();
    String indent = enableIndent ? getIndent(format) : "";
    String newline = enableIndent ? "\n" : "";

    String tagName = getName().getLocalName();

    StringBuilder sb = new StringBuilder();
    sb.append(indent);
    sb.append('<');
    sb.append(tagName);

    for (XAttribute att : attributes) {
      sb.append(' ');
      sb.append(att.getName().getLocalName());
      sb.append("=\"");
      sb.append(XmlUtil.escapeAttributeValue(att.getValue()));
      sb.append('"');
    }

    List<XNode> nodes = nodes().toList();
    if (nodes.size() == 0) {
      sb.append(" />");
    } else {
      sb.append('>');
      boolean onlyText = true;
      for (XNode node : nodes()) {
        if (node.getNodeType() == XmlNodeType.TEXT) {
          sb.append(node.toString(format));
        } else {
          onlyText = false;
          sb.append(newline);
          sb.append(node.toString(format.incrementLevel()));
        }
      }
      if (!onlyText) {
        sb.append(newline + indent);
      }
      sb.append("</");
      sb.append(tagName);
      sb.append('>');
    }
    return sb.toString();
  }

  public void setAttributeValue(XName attributeName, String value) {
    for (XAttribute attribute : attributes) {
      if (attribute.getName().equals(attributeName)) {
        attribute.setValue(value);
        return;
      }
    }
  }

  public void removeAttribute(XName attributeName) {
    for (ListIterator<XAttribute> iterator = attributes.listIterator(attributes.size()); iterator.hasPrevious();){
      if (iterator.previous().getName().equals(attributeName)) {
        iterator.remove();
        return;
      }
    }
  }

  public void removeAttributes() {
    this.attributes.clear();
  }

  public String getValue() {
    XText firstText = nodes().ofType(XText.class).firstOrNull();
    return firstText == null ? null : firstText.getValue();
  }
}
