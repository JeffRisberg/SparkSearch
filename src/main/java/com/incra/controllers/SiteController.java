package com.incra.controllers;

import com.incra.models.Site;
import com.incra.services.PageFrameworkService;
import com.incra.services.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The <i>SiteController</i> controller implements CRUD operations on Sites.
 *
 * @author Jeffrey Risberg
 * @since 03/12/14
 */
@Controller
public class SiteController extends AbstractAdminController {
    protected static Logger logger = LoggerFactory.getLogger(SiteController.class);

    @Autowired
    private SiteService siteService;
    @Autowired
    private PageFrameworkService pageFrameworkService;

    public SiteController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) throws Exception {
        dataBinder.registerCustomEditor
                (Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"), false));
    }

    @RequestMapping(value = "/site/**")
    public String index() {
        return "redirect:/site/list";
    }

    @RequestMapping(value = "/site/list")
    public ModelAndView list(Object criteria) {

        List<Site> siteList = siteService.findEntityList();

        ModelAndView modelAndView = new ModelAndView("site/list");
        modelAndView.addObject("siteList", siteList);
        return modelAndView;
    }

    @RequestMapping(value = "/site/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable int id, Model model, HttpSession session) {

        Site site = siteService.findEntityById(id);
        if (site != null) {
            model.addAttribute(site);
            return "site/show";
        } else {
            pageFrameworkService.setFlashMessage(session, "No Site with that id");
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
            return "redirect:/site/list";
        }
    }

    @RequestMapping(value = "/site/create", method = RequestMethod.GET)
    public ModelAndView create() {

        Site site = new Site();

        ModelAndView modelAndView = new ModelAndView("site/create");
        modelAndView.addObject("command", site);

        return modelAndView;
    }

    @RequestMapping(value = "/site/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable int id) {
        Site site = siteService.findEntityById(id);

        ModelAndView modelAndView = new ModelAndView("site/edit");
        modelAndView.addObject("command", site);

        return modelAndView;
    }

    @RequestMapping(value = "/site/save", method = RequestMethod.POST)
    public String save(final @ModelAttribute("command") @Valid Site site,
                       @RequestParam("imageFile") MultipartFile imageFile,
                       BindingResult result, Model model, HttpSession session) {

        if (result.hasErrors()) {
            for (ObjectError error: result.getAllErrors() ) {
                System.out.println(error);
            }

            if (site.getDateCreated() == null) site.setDateCreated(new Date());
            return "site/edit";
        }

        String name = "blah";
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());

                System.out.println("You successfully uploaded file=" + name);
            } catch (Exception e) {
                System.out.println("You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            System.out.println("You failed to upload " + name
                    + " because the file was empty.");
        }

        try {
            if (site.getDateCreated() == null) site.setDateCreated(getNow());
            site.setLastUpdated(getNow());

            siteService.save(site);
        } catch (RuntimeException re) {
            pageFrameworkService.setFlashMessage(session, re.getMessage());
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
            return "redirect:/site/list";
        }
        return "redirect:/site/list";
    }

    @RequestMapping(value = "/site/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id, HttpSession session) {

        Site site = siteService.findEntityById(id);
        if (site != null) {
            try {
                siteService.delete(site);
            } catch (RuntimeException re) {
                pageFrameworkService.setFlashMessage(session, re.getMessage());
                pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
                return "redirect:/site/show/" + id;
            }
        } else {
            pageFrameworkService.setFlashMessage(session, "No Site with that id");
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
        }

        return "redirect:/site/list";
    }
}