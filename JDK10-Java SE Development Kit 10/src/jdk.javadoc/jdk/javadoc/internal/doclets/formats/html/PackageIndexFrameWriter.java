/*
 * Copyright (c) 1998, 2017, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.javadoc.internal.doclets.formats.html;

import java.util.Collection;

import javax.lang.model.element.PackageElement;

import jdk.javadoc.internal.doclets.formats.html.markup.HtmlConstants;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTag;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTree;
import jdk.javadoc.internal.doclets.formats.html.markup.Links;
import jdk.javadoc.internal.doclets.formats.html.markup.RawHtml;
import jdk.javadoc.internal.doclets.formats.html.markup.StringContent;
import jdk.javadoc.internal.doclets.toolkit.Content;
import jdk.javadoc.internal.doclets.toolkit.util.DocFileIOException;
import jdk.javadoc.internal.doclets.toolkit.util.DocPath;
import jdk.javadoc.internal.doclets.toolkit.util.DocPaths;


/**
 * Generate the package index for the left-hand frame in the generated output.
 * A click on the package name in this frame will update the page in the bottom
 * left hand frame with the listing of contents of the clicked package.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Atul M Dambalkar
 */
public class PackageIndexFrameWriter extends AbstractPackageIndexWriter {

    /**
     * Construct the PackageIndexFrameWriter object.
     *
     * @param filename Name of the package index file to be generated.
     */
    public PackageIndexFrameWriter(HtmlConfiguration configuration, DocPath filename) {
        super(configuration, filename);
    }

    /**
     * Generate the package index file named "overview-frame.html".
     * @throws DocFileIOException
     */
    public static void generate(HtmlConfiguration configuration) throws DocFileIOException {
        DocPath filename = DocPaths.OVERVIEW_FRAME;
        PackageIndexFrameWriter packgen = new PackageIndexFrameWriter(configuration, filename);
        packgen.buildPackageIndexFile("doclet.Window_Overview", false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPackagesList(Content body) {
        Content heading = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true,
                contents.packagesLabel);
        HtmlTree htmlTree = (configuration.allowTag(HtmlTag.MAIN))
                ? HtmlTree.MAIN(HtmlStyle.indexContainer, heading)
                : HtmlTree.DIV(HtmlStyle.indexContainer, heading);
        HtmlTree ul = new HtmlTree(HtmlTag.UL);
        ul.setTitle(contents.packagesLabel);
        for (PackageElement aPackage : packages) {
            // Do not list the package if -nodeprecated option is set and the
            // package is marked as deprecated.
            if (aPackage != null &&
                (!(configuration.nodeprecated && utils.isDeprecated(aPackage)))) {
                ul.addContent(getPackage(aPackage));
            }
        }
        htmlTree.addContent(ul);
        body.addContent(htmlTree);
    }

    /**
     * Returns each package name as a separate link.
     *
     * @param pe PackageElement
     * @return content for the package link
     */
    protected Content getPackage(PackageElement pe) {
        Content packageLinkContent;
        Content packageLabel;
        if (pe.isUnnamed()) {
            packageLabel = new StringContent("<unnamed package>");
            packageLinkContent = Links.createLink(DocPaths.PACKAGE_FRAME,
                    packageLabel, "", "packageFrame");
        } else {
            packageLabel = getPackageLabel(pe.getQualifiedName());
            packageLinkContent = Links.createLink(pathString(pe,
                     DocPaths.PACKAGE_FRAME), packageLabel, "",
                    "packageFrame");
        }
        Content li = HtmlTree.LI(packageLinkContent);
        return li;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addNavigationBarHeader(Content body) {
        Content headerContent;
        if (configuration.packagesheader.length() > 0) {
            headerContent = new RawHtml(replaceDocRootDir(configuration.packagesheader));
        } else {
            headerContent = new RawHtml(replaceDocRootDir(configuration.header));
        }
        Content heading = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true,
                HtmlStyle.bar, headerContent);
        body.addContent(heading);
    }

    /**
     * Do nothing as there is no overview information in this page.
     */
    @Override
    protected void addOverviewHeader(Content body) {
    }

    /**
     * Adds "All Classes" link for the top of the left-hand frame page to the
     * documentation tree.
     *
     * @param ul the Content object to which the "All Classes" link should be added
     */
    @Override
    protected void addAllClassesLink(Content ul) {
        Content linkContent = Links.createLink(DocPaths.ALLCLASSES_FRAME,
                contents.allClassesLabel, "", "packageFrame");
        Content li = HtmlTree.LI(linkContent);
        ul.addContent(li);
    }

    /**
     * Adds "All Modules" link for the top of the left-hand frame page to the
     * documentation tree.
     *
     * @param ul the Content object to which the "All Modules" link should be added
     */
    @Override
    protected void addAllModulesLink(Content ul) {
        Content linkContent = Links.createLink(DocPaths.MODULE_OVERVIEW_FRAME,
                contents.allModulesLabel, "", "packageListFrame");
        Content li = HtmlTree.LI(linkContent);
        ul.addContent(li);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addNavigationBarFooter(Content body) {
        Content p = HtmlTree.P(Contents.SPACE);
        body.addContent(p);
    }
}
