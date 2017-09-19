package com.incra.controllers;

import com.incra.models.Origin;
import com.incra.services.OriginService;
import com.incra.services.PageFrameworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The <i>OriginController</i> controller implements CRUD operations on Origins.
 *
 * @author Jeffrey Risberg
 * @since 03/12/14
 */
@Controller
public class OriginController extends AbstractAdminController {
    protected static Logger logger = LoggerFactory.getLogger(OriginController.class);

    @Autowired
    private OriginService originService;
    @Autowired
    private PageFrameworkService pageFrameworkService;

    public OriginController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) throws Exception {
        dataBinder.registerCustomEditor
                (Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"), false));
    }

    @RequestMapping(value = "/Origin/**")
    public String index() {
        return "redirect:/Origin/list";
    }

    @RequestMapping(value = "/Origin/list")
    public ModelAndView list(Object criteria) {

        List<Origin> FacilityList = originService.findEntityList();

        ModelAndView modelAndView = new ModelAndView("Origin/list");
        modelAndView.addObject("FacilityList", FacilityList);
        return modelAndView;
    }

    @RequestMapping(value = "/Origin/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable int id, Model model, HttpSession session) {

        Origin Origin = originService.findEntityById(id);
        if (Origin != null) {
            model.addAttribute(Origin);
            return "Origin/show";
        } else {
            pageFrameworkService.setFlashMessage(session, "No Origin with that id");
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
            return "redirect:/Origin/list";
        }
    }

    @RequestMapping(value = "/Origin/create", method = RequestMethod.GET)
    public ModelAndView create() {

        Origin Origin = new Origin();

        ModelAndView modelAndView = new ModelAndView("Origin/create");
        modelAndView.addObject("command", Origin);
        return modelAndView;
    }

    @RequestMapping(value = "/Origin/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable int id, String finalURL) {
        Origin Origin = originService.findEntityById(id);

        ModelAndView modelAndView = new ModelAndView("Origin/edit");
        modelAndView.addObject("command", Origin);
        modelAndView.addObject("finalURL", finalURL);

        return modelAndView;
    }

    @RequestMapping(value = "/Origin/save", method = RequestMethod.POST)
    public String save(final @ModelAttribute("command") @Valid Origin Origin, String finalURL,
                       BindingResult result, Model model, HttpSession session) {

        if (result.hasErrors()) {
            return "Origin/edit";
        }

        try {
            if (Origin.getDateCreated() == null) Origin.setDateCreated(new Date());
            Origin.setLastUpdated(new Date());

            originService.save(Origin);
        } catch (RuntimeException re) {
            pageFrameworkService.setFlashMessage(session, re.getMessage());
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
            return "redirect:/Origin/list";
        }
        if (finalURL != null && finalURL.length() > 0) {
            return "redirect:" + finalURL;
        } else {
            return "redirect:/Origin/list";
        }
    }

    @RequestMapping(value = "/Origin/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id, HttpSession session) {

        Origin Origin = originService.findEntityById(id);
        if (Origin != null) {
            try {
                originService.delete(Origin);
            } catch (RuntimeException re) {
                pageFrameworkService.setFlashMessage(session, re.getMessage());
                pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
                return "redirect:/Origin/show/" + id;
            }
        } else {
            pageFrameworkService.setFlashMessage(session, "No Origin with that id");
            pageFrameworkService.setIsRedirect(session, Boolean.TRUE);
        }

        return "redirect:/origin/list";
    }
}