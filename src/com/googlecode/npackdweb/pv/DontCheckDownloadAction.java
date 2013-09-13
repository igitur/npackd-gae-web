package com.googlecode.npackdweb.pv;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.npackdweb.DefaultServlet;
import com.googlecode.npackdweb.MessagePage;
import com.googlecode.npackdweb.NWUtils;
import com.googlecode.npackdweb.db.Package;
import com.googlecode.npackdweb.db.PackageVersion;
import com.googlecode.npackdweb.wlib.Action;
import com.googlecode.npackdweb.wlib.ActionSecurityType;
import com.googlecode.npackdweb.wlib.Page;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

/**
 * Disable download check for a package version
 */
public class DontCheckDownloadAction extends Action {
    /**
     * -
     */
    public DontCheckDownloadAction() {
        super("^/package-version/dont-check-download$",
                ActionSecurityType.LOGGED_IN);
    }

    @Override
    public Page perform(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String package_ = req.getParameter("package");
        String version = req.getParameter("version");
        Objectify ofy = DefaultServlet.getObjectify();
        Package pa = ofy.get(new Key<Package>(Package.class, package_));
        Page page;
        if (!pa.isCurrentUserPermittedToModify())
            page = new MessagePage(
                    "You do not have permission to modify this package");
        else {
            PackageVersion p = ofy.get(new Key<PackageVersion>(
                    PackageVersion.class, package_ + "@" + version));
            p.downloadCheckError = PackageVersion.DONT_CHECK_THIS_DOWNLOAD;
            p.downloadCheckAt = new Date();
            NWUtils.savePackageVersion(ofy, p);
            resp.sendRedirect("/p/" + p.package_ + "/" + p.version);
            page = null;
        }
        return page;
    }
}