package org.core4j.xml;

/**
 * Created by ciddan on 2014-01-27.
 */
public class XCData extends XNode {
  private String value;

  public XCData(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public XmlNodeType getNodeType() {
    return XmlNodeType.TEXT;
  }

  @Override
  public String toString() {
    return toString(XmlFormat.NOT_INDENTED);
  }

  @Override
  public String toString(XmlFormat format) {
    return format.isIndentEnabled()
      ? XmlUtil.cdataEscapeValue(value.trim())
      : XmlUtil.cdataEscapeValue(value);
  }
}
