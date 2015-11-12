/*******************************************************************************
 * ${licenseText}
 * All rights reserved. This file is made available under the terms of the 
 * Common Development and Distribution License (CDDL) v1.0 which accompanies 
 * this distribution, and is available at 
 * http://www.opensource.org/licenses/cddl1.txt     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.util;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * A builder for XSL-FO documents. This builder provides a simple way
 * to build a flow of pages, and internally maintains a DOM with the according
 * XSL-FO elements. When finished with adding content, call <code>createDocument()</code>
 * to receive the DOM object.
 */
public class XslFoDocumentBuilder {
	
	private Document document;
	
	private Element flow;
	
	private static final Namespace ns = Namespace.getNamespace("fo", "http://www.w3.org/1999/XSL/Format");

	protected static final String EMPTY_DTD = "<!ELEMENT svg ANY>";
	
	public XslFoDocumentBuilder() {
		document = new Document();
		
		// create root element
		Element e = new Element("root", ns);
		
		document.addContent(e);
	}
	
	public void addPageMaster(String name, float widthMM, float heightMM) {
		Element m = new Element("simple-page-master", ns);
		m.setAttribute("master-name", name);
		m.setAttribute("page-width", widthMM + "mm");
		m.setAttribute("page-height", heightMM + "mm");
		m.setAttribute("margin", "0mm");
		
		Element body = new Element("region-body", ns);
		m.addContent(body);
		
		// insert into master set, if present
		Element ms = document.getRootElement().getChild("layout-master-set", ns);
		if (ms == null) {
			ms = new Element("layout-master-set", ns);
			document.getRootElement().addContent(ms);
		}
		
		ms.addContent(m);
	}
	
	public void startFlow(String masterName) {
		if (flow != null)
			throw new IllegalStateException("Please call endFlow() first before starting a new flow!");
		
		Element ps = new Element("page-sequence", ns);
		ps.setAttribute("master-reference", masterName);
		ps.setAttribute("id", masterName);
		Element f = new Element("flow", ns);
		f.setAttribute("flow-name", "xsl-region-body");
		ps.addContent(f);
		document.getRootElement().addContent(ps);
		flow = f;
	}
	
	public void endFlow() {
		flow = null;
	}
	
	public void newPage() {
		if (flow == null)
			throw new IllegalStateException("Please call startFlow() first");
		
		Element e = new Element("block", ns);
		e.setAttribute("break-after", "page");
		e.setAttribute("padding", "0mm");
		e.setAttribute("margin", "0mm");
		e.setAttribute("border-width", "0mm");
		flow.addContent(e);
	}
	
	public Document createDocument() {
		return (Document)document.clone();
	}

	public Namespace getNamespace() {
		return ns;
	}

	public void addPageElement(Element element, float widthMM, float heightMM) {
		if (flow == null)
			throw new IllegalStateException("Please call startFlow() first");
		
		// create block container
		Element bc = new Element("block-container",ns);
		bc.setAttribute("absolute-position", "absolute");
		bc.setAttribute("left", "0mm");
		bc.setAttribute("top", "0mm");
		bc.setAttribute("width", widthMM + "mm");
		bc.setAttribute("height", heightMM + "mm");
		
		// create block
		Element b = new Element("block", ns);
		b.setAttribute("margin", "0mm");
		b.setAttribute("padding", "0mm");
		b.setAttribute("border-width", "0mm");
		bc.addContent(b);
		
		b.addContent(element);
		
		flow.addContent(bc);
	}	

}
