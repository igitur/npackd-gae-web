package com.googlecode.npackdweb;

import com.googlecode.npackdweb.db.License;
import com.googlecode.npackdweb.db.Package;
import com.googlecode.npackdweb.db.PackageVersion;
import com.googlecode.npackdweb.wlib.Action;
import com.googlecode.npackdweb.wlib.ActionSecurityType;
import com.googlecode.npackdweb.wlib.Page;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Edit a package, a package version or a complete repository as XML.
 */
public class EditAsXMLAction extends Action {

    /**
     * -
     */
    public EditAsXMLAction() {
        super("^/rep/edit-as-xml$", ActionSecurityType.ANONYMOUS);
    }

    @Override
    public Page perform(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String type = req.getParameter("type");
        String package_ = req.getParameter("package");
        String version = req.getParameter("version");
        String id = req.getParameter("id");

        if (type == null) {
            type = "old";
        }

        Document d = NWUtils.newXMLRepository(false);

        Objectify ofy = ofy();
        Element root = d.getDocumentElement();
        String tag = "";

        switch (type) {
            case "version": {
                PackageVersion r =
                        ofy.load().key(Key.create(
                                        PackageVersion.class, id)).now();
                Element e = r.toXML(d);
                if (r.tags.size() > 0) {
                    tag = r.tags.get(0);
                }
                root.appendChild(e);
                break;
            }
            case "package": {
                Package r = ofy.load().key(Key.create(Package.class, id)).
                        now();
                Element e = r.toXML(d);
                root.appendChild(e);
                break;
            }
            case "license": {
                License r = ofy.load().key(Key.create(License.class, id)).
                        now();
                Element e = r.toXML(d);
                root.appendChild(e);
                break;
            }
            default: {
                if (package_ == null) {
                    // nothing. Editing an empty repository.
                } else if (version == null) {
                    Package r = ofy.load().key(Key.create(Package.class,
                            package_)).now();
                    Element e = r.toXML(d);
                    root.appendChild(e);
                } else {
                    PackageVersion r =
                            ofy.load().key(Key.create(
                                            PackageVersion.class,
                                            package_ + "@" + version)).now();
                    Element e = r.toXML(d);
                    if (r.tags.size() > 0) {
                        tag = r.tags.get(0);
                    }
                    root.appendChild(e);
                }
            }
        }

        return new EditAsXMLPage(d, tag);
    }
}
