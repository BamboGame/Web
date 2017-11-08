package com.iluso.controllers;

import com.iluso.entities.ChargeRequest;

import com.iluso.entities.ChargeRequest.Currency;
import com.iluso.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iluso.entities.paypal.PayPalSuccess;

@Controller
public class DonationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StripeService paymentsService = new StripeService();
    
    @RequestMapping("/donation")
    public String displayPage(Model model)
    {
    	return "Donation/donation";
    }
    
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public String charge(@RequestParam("amount") int amount,@RequestParam("email") String email, @RequestParam("stripeToken") String stripeToken, ChargeRequest chargeRequest, Model model) throws StripeException
    {
        //model.addAttribute("message"," again");
        chargeRequest.setAmount(amount);
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.EUR);
        chargeRequest.setStripeEmail(email);
        chargeRequest.setStripeTocken(stripeToken);
        Charge charge = paymentsService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        
        return "Donation/success";
    }
    
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String Succes(ModelMap modelmap,HttpServletRequest request){
        PayPalSuccess ps=new PayPalSuccess();
        modelmap.put("result", ps.getPayPal(request));
        return "Donation/paypalsuccess";
        
    }
}
