package com.googlecode.npackdweb;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * Delete package version confirmation.
 */
public class PackageVersionDeletePage extends FramePage {
	private PackageVersion pv;

	/**
	 * @param pv
	 *            this version should be deleted
	 */
	public PackageVersionDeletePage(PackageVersion pv) {
		this.pv = pv;
	}

	@Override
	public String createContent(HttpServletRequest request) throws IOException {
		HTMLWriter w = new HTMLWriter();
		w.start("form", "method", "post", "action",
				"/package-version/delete-confirmed");
		w.e("div", "Do you really want to delete " + pv.package_ + " "
				+ pv.version + "?");
		w.e("input", "type", "hidden", "name", "name", "value", pv.name);
		w.e("input", "class", "input", "type", "submit", "value", "Delete");
		w.e("input", "class", "input", "type", "button", "value", "Cancel",
				"onclick", "window.location.href='/p/" + pv.package_ + "/"
						+ pv.version + "'");
		w.end("form");
		return w.toString();
	}

	@Override
	public String getTitle() {
		return "Confirmation";
	}
}
