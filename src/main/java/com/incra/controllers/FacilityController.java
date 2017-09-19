package com.incra.controllers;

import com.incra.models.Facility;
import com.incra.models.Site;
import com.incra.services.FacilityService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The <i>FacilityController</i> controller implements CRUD operations on Facilityes.
 *
 * @author Jeffrey Risberg
 * @since 03/12/14
 */
@Controller
public class FacilityController extends AbstractAdminController {
    protected static Logger logger = LoggerFactory.getLogger(FacilityController.class);

    @Autowired
    private FacilityService facilityService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private PageFrameworkService pageFrameworkService;

    public FacilityController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) throws Exception {
        dataBinder.registerCustomEditor
                (Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"), false));
        //dataBinder.registerCustomEditor(Site.class,
        //        new SitePropertyEditor(siteService));
    }

    @RequestMapping(value = "/Facility/**")
    public String index() {
        return "redirect:/Facility/list";
    }

    @RequestMapping(value = "/Facility/list")
    public ModelAndView list(Object criteria) {

        List<Facility> FacilityList = facilityService.findEntityList();

        ModelAndView modelAndView = new ModelAndView("Facility/list");
        modelAndView.addObject("FacilityList", FacilityList);
        return modelAndView;
    }

    @RequestMapping(value = "/Facility/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable int id, Model model, HttpSession session) {

        Facility facility = facilityService.findEntityById(id);
        if (facility != null) {
            model.addAttribute(facility);
            return "Facility/show";
        } else {
            pageFrameworkService.setFlashMessage(session, "No Facility with that id");
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
            return "redirect:/facility/list";
        }
    }

    @RequestMapping(value = "/Facility/create", method = RequestMethod.GET)
    public ModelAndView create() {

        Facility facility = new Facility();
        List<Site> siteList = siteService.findEntityList();

        ModelAndView modelAndView = new ModelAndView("Facility/create");
        modelAndView.addObject("siteList", siteList);
        modelAndView.addObject("command", facility);
        return modelAndView;
    }

    @RequestMapping(value = "/facility/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable int id) {

        Facility facility = facilityService.findEntityById(id);
        List<Site> siteList = siteService.findEntityList();

        ModelAndView modelAndView = new ModelAndView("Facility/edit");
        modelAndView.addObject("siteList", siteList);
        modelAndView.addObject("command", facility);

        return modelAndView;
    }

    @RequestMapping(value = "/facility/save", method = RequestMethod.POST)
    public String save(final @ModelAttribute("command") @Valid Facility facility,
                       BindingResult result, Model model, HttpSession session) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error);
            }

            List<Site> siteList = siteService.findEntityList();
            model.addAttribute("siteList", siteList);

            if (facility.getDateCreated() == null) facility.setDateCreated(new Date());
            return "Facility/edit";
        }

        try {
            if (facility.getDateCreated() == null) facility.setDateCreated(new Date());
            facility.setLastUpdated(new Date());

            facilityService.save(facility);
        } catch (RuntimeException re) {
            pageFrameworkService.setFlashMessage(session, re.getMessage());
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
            return "redirect:/Facility/list";
        }
        return "redirect:/Facility/list";
    }

    @RequestMapping(value = "/Facility/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id, HttpSession session) {

        Facility facility = facilityService.findEntityById(id);
        if (facility != null) {
            try {
                facilityService.delete(facility);
            } catch (RuntimeException re) {
                pageFrameworkService.setFlashMessage(session, re.getMessage());
                pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
                return "redirect:/Facility/show/" + id;
            }
        } else {
            pageFrameworkService.setFlashMessage(session, "No Facility with that id");
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
        }

        return "redirect:/Facility/list";
    }
}