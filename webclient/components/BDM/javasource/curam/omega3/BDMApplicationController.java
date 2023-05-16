package curam.omega3;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import curam.omega3.user.UserPreferencesFactory;

public class BDMApplicationController extends ApplicationController {

	protected void doLogout(HttpServletRequest request, HttpServletResponse response, String uri)
			throws ServletException, IOException, UnsupportedEncodingException {

		UserPreferencesFactory.getUserPreferences(request.getSession()).getTabListing().flushOnLogout();
		request.logout();
		request.getSession().invalidate();

		response.sendRedirect("https://esdc.prv/en/index.shtml");

	}
}
