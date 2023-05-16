package curam.ca.bdm.widget.render;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import curam.omega3.config.appconfig.AddressFormat;
import curam.omega3.taglib.formwidget.CuramAddress;
import curam.util.client.ClientException;
import curam.util.client.model.Component;
import curam.util.client.model.ComponentBuilder;
import curam.util.client.model.ComponentBuilderFactory;
import curam.util.client.model.Container;
import curam.util.client.model.ContainerBuilder;
import curam.util.client.model.Field;
import curam.util.client.model.FieldBuilder;
import curam.util.client.model.PathTextBuilder;
import curam.util.client.model.Text;
import curam.util.client.model.TextBuilderFactory;
import curam.util.client.path.util.ClientPaths;
import curam.util.client.view.RendererContext;
import curam.util.client.view.RendererContract;
import curam.util.common.domain.DomainException;
import curam.util.common.path.DataAccessException;
import curam.util.common.path.DataAccessor;
import curam.util.common.path.Path;
import curam.util.common.plugin.PlugInException;
import curam.util.render.address.DefaultAddressEditRenderer;

public class BDMAddressEditRender extends DefaultAddressEditRenderer {

	/**
	 * Search Address properties path.
	 */
	public static final String SEARCH_ADDRESS_PROP_PATH = "curam.omega3.i18n.SearchAddress";
	public String initialCountryValue = "";
	public int countryPosition = -1;
	String baseID;
	String countryID = "";

	@Override
	public void render(Field field, DocumentFragment fragment, RendererContext context, RendererContract contract)
			throws ClientException, DataAccessException, PlugInException {

		/*
		 * BEGIN - CKwong - Task 5155 Customization Address Evidence
		 */

		this.renderPage(field, fragment, context, contract);
		// Find the first node in the address details. This will be a hidden input node.
		final Element firstElement = (Element) fragment.getFirstChild();

		// calculate the row and column position of the country element based off the
		// index
		if (countryPosition >= 0) {
			NodeList rowChildNodes = fragment.getChildNodes();
			double rowInd = Math.floorDiv(countryPosition, 2) + 1;
			int colInd = (countryPosition % 2 == 0) ? 0 : 1;

			Node countryRow = rowChildNodes.item((int) rowInd);
			NodeList countryChildNodes = countryRow.getChildNodes();
			Element countryColumn = (Element) countryChildNodes.item(colInd);

			Element dupColumn = (Element) countryColumn.cloneNode(true);

			dupColumn.setAttribute("id", countryID);
			countryColumn.getParentNode().replaceChild(dupColumn, countryColumn);
		}

		// Insert search container node before the first table row. The first table row
		// will be after the hidden input node.
		fragment.insertBefore(createSearchContainer(fragment, field, baseID, context), firstElement.getNextSibling());

		// Append the java script file used for address processing.
		final Element jsElem0;
		final Document doc = fragment.getOwnerDocument();

		jsElem0 = doc.createElement("script");
		jsElem0.setAttribute("type", "text/javascript");
		jsElem0.setAttribute("src", "../CDEJ/jscript/BDMSearchAddress.js");
		jsElem0.appendChild(doc.createComment("Search Address js"));
		fragment.appendChild(jsElem0);

		// Append the js file used for dynamic address rendering
		final Element jsElem1;
		jsElem1 = doc.createElement("script");
		jsElem1.setAttribute("type", "text/javascript");
		jsElem1.setAttribute("src", "../CDEJ/jscript/BDMRenderAddress.js");
		jsElem1.appendChild(doc.createComment("Render Address js"));
		fragment.appendChild(jsElem1);

		// Call initial load for initial rendering of address, passing in country ID to
		// the cluster in focus and the initial country value
		final Element jsElem2;
		jsElem2 = doc.createElement("script");
		jsElem2.appendChild(
				doc.createTextNode("initialLoad" + "(\"" + countryID + "\",\"" + initialCountryValue + "\")"));
		fragment.appendChild(jsElem2);

		/*
		 * BEGIN - CKwong - Task 5155 Customization Address Evidence
		 */

	}

	/**
	 * Create label and button for the address search.
	 *
	 * @param fragment Document fragment.
	 * @param field    Field details.
	 * @param baseID   Contains base ID for this address. This ID will be used to
	 *                 generate ID's of the new elements being added.
	 *
	 * @return Created HTML table row containing address search label and icon.
	 * @throws ClientException     Generic client exception signature.
	 * @throws DataAccessException
	 */
	private Element createSearchContainer(DocumentFragment fragment, Field field, final String baseID,
			final RendererContext context) throws ClientException, DataAccessException {
		/* BEGIN - CKwong - Task 5155 Customization Address Evidence */
		String bdmAddressClass = "bdm-address-search";
		String bdmAddressIconClass = "bdm-address-search-icon";

		Path dtlsPath = ClientPaths.GENERAL_RESOURCES_PATH.extendPath(SEARCH_ADDRESS_PROP_PATH);

		DataAccessor da = context.getDataAccessor();

		String searchAddressLabel = da.get(dtlsPath.extendPath("search.address.label"));
		String searchIconLabel = da.get(dtlsPath.extendPath("search.icon.label"));

		final Document doc = fragment.getOwnerDocument();
		final Element searchRow = doc.createElement("div");
		searchRow.setAttribute("class", "bx--row");

		final Element searchCol = doc.createElement("div");
		searchCol.setAttribute("class", "bx--col " + bdmAddressClass);

		final String searchHeaderID = "sahc" + baseID;

		// Create search button
		final Element searchButtonCol = doc.createElement("td");
		searchButtonCol.setAttribute("class", "address");
		searchButtonCol.setAttribute("headers", searchHeaderID);

		final Element anchor = doc.createElement("a");
		anchor.setAttribute("class", "popup-field-pclookup-search " + bdmAddressClass + " " + bdmAddressIconClass);
		anchor.setAttribute("href", "#");

		// An ID is required to uniquely identify address object.
		// We can use ID of hidden address input node as a basis for generating ID for
		// the anchor.
		final String anchorID = "al" + baseID;
		String countryID = "country_" + baseID;
		anchor.setAttribute("id", anchorID);
		anchor.setAttribute("onClick", "execPopupForAddress(this,\"" + countryID +"\")");

		final Element img = doc.createElement("img");
		img.setAttribute("src", "../themes/curam/images/search--20-enabled.svg");
		img.setAttribute("title", searchIconLabel);
		img.setAttribute("alt", "search icon");

		anchor.appendChild(img);

		searchButtonCol.appendChild(anchor);

		searchCol.appendChild(searchButtonCol);

		// Create search label.
		final Element searchTitleCol = doc.createElement("div");
		searchTitleCol.setAttribute("id", searchHeaderID);
		searchTitleCol.setAttribute("class", "bx--label " + bdmAddressClass);

		final Element searchLabel = doc.createElement("span");
		searchLabel.setAttribute("title", searchAddressLabel);
		searchLabel.setAttribute("class", bdmAddressClass);
		searchLabel.appendChild(doc.createTextNode(searchAddressLabel));

		searchTitleCol.appendChild(searchLabel);

		searchCol.appendChild(searchTitleCol);
		// Create search button.

		searchRow.appendChild(searchCol);

		return searchRow;

		/* END - CKwong - Task 5155 Customization Address Evidence */

	}

	/**
	 * Copy of the default AddressEditRenderer renderPage method, with modification
	 * to pass in the hashed countryID.
	 * 
	 * @param field
	 * @param fragment
	 * @param context
	 * @param contract
	 * @param countryID
	 * @throws ClientException
	 * @throws DataAccessException
	 * @throws PlugInException
	 */
	private void renderPage(Field field, DocumentFragment fragment, RendererContext context, RendererContract contract)
			throws ClientException, DataAccessException, PlugInException {
		Path formatPath = ClientPaths.ADDRESS_FORMAT_PATH.extendPath(new String[] { "format[][]" });

		boolean hasSource = field.getBinding().getSourcePath() != null;
		boolean hasTarget = field.getBinding().getTargetPath() != null;

		String addressFormatName;
		if (hasSource) {
			addressFormatName = context.getDataAccessor()
					.get(field.getBinding().getSourcePath().extendPath(new String[] { "format" }));

		} else {
			addressFormatName = context.getDataAccessor()
					.get(ClientPaths.ADDRESS_FORMAT_PATH.extendPath(new String[] { "default-format-name" }));
		}

		formatPath = formatPath.applyIndex(2, addressFormatName, false);

		this.createAddressHeadingDescription(context, fragment);

		/* BEGIN - CKwong - Task 5155 Customization Address Evidence */
		Element header = null;
		if (hasTarget) {
			header = this.createInputHeader(context, fragment, field, addressFormatName, hasSource);

		}

		// add countryID to method - called after header has been created so that
		// countryID can be populated correctly
		Container innerClusterBody = this.createAddressFieldCluster(context, field, formatPath, hasTarget, hasSource);
		if (header != null) {
			fragment.appendChild(header);
		}

		/* END - CKwong - Task 5155 Customization Address Evidence */

		DocumentFragment fieldFragment = fragment.getOwnerDocument().createDocumentFragment();
		context.render(innerClusterBody, fieldFragment, contract.createSubcontract());

		fragment.appendChild(fieldFragment);
	}

	/**
	 * Copy of default address edit renderer method
	 * 
	 * @param context
	 * @param fragment
	 */
	private void createAddressHeadingDescription(RendererContext context, DocumentFragment fragment) {
		String addressDescription = "";

		try {
			String AddressDescriptionParameter = context.getDataAccessor()
					.get(ClientPaths.ADDRESS_FORMAT_PATH.extendPath(new String[] { "description" }));
			addressDescription = context.getDataAccessor()
					.get(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { AddressDescriptionParameter }));

		} catch (DataAccessException var8) {

			addressDescription = "";
		}

		if (addressDescription.length() > 0) {

			Element description = fragment.getOwnerDocument().createElement("p");
			description.setTextContent(addressDescription);
			description.setAttribute("class", "bx--address__mandatory-description");

			Element headerCell = fragment.getOwnerDocument().createElement("th");
			headerCell.setAttribute("colspan", "4");

			Element tableRow = fragment.getOwnerDocument().createElement("tr");

			Element tableHeader = fragment.getOwnerDocument().createElement("thead");

			headerCell.appendChild(description);
			tableRow.appendChild(headerCell);
			tableHeader.appendChild(tableRow);
			fragment.appendChild(tableHeader);
		}
	}

	/**
	 * Copy of default address edit renderer method
	 * 
	 * @param context
	 * @param fragment
	 * @param field
	 * @param addressFormatName
	 * @param hasSource
	 * @return
	 * @throws ClientException
	 * @throws DataAccessException
	 */
	private Element createInputHeader(RendererContext context, DocumentFragment fragment, Field field,
			String addressFormatName, boolean hasSource) throws ClientException, DataAccessException {
		Element header = fragment.getOwnerDocument().createElement("input");
		String headerTargetID = context.addFormItem(field, (String) null, "header");
		baseID = headerTargetID;
		header.setAttribute("id", headerTargetID);
		header.setAttribute("name", headerTargetID);
		header.setAttribute("type", "hidden");
		header.setAttribute("dcl-blankable", "false");
		if (hasSource) {
			header.setAttribute("value", context.getDataAccessor()
					.get(field.getBinding().getSourcePath().extendPath(new String[] { "header" })));
		} else {
			AddressFormat format = new AddressFormat();
			format.setName(addressFormatName);

			format.setCountryCode(context.getDataAccessor()
					.get(ClientPaths.ADDRESS_FORMAT_PATH.extendPath(new String[] { "default-country-code" })));

			header.setAttribute("value", (new CuramAddress(format)).getAddressHeader());
		}
		return header;
	}

	/**
	 * Copy of default address edit renderer method with slight modification to add
	 * an event listener to the country field.
	 * 
	 * @param context
	 * @param field
	 * @param formatPath
	 * @param hasTarget
	 * @param hasSource
	 * @return
	 * @throws ClientException
	 * @throws DataAccessException
	 * @throws PlugInException
	 */
	private Container createAddressFieldCluster(RendererContext context, Field field, Path formatPath,
			boolean hasTarget, boolean hasSource) throws ClientException, DataAccessException, PlugInException {
		// Country element name
		String countryFieldName = "COUNTRY";

		ContainerBuilder clusterBuilder = ComponentBuilderFactory.createContainerBuilder();
		ComponentBuilder<Component> fHeadingBuilder = ComponentBuilderFactory.createComponentBuilder();
		FieldBuilder fBuilder = ComponentBuilderFactory.createFieldBuilder();
		PathTextBuilder tBuilder = TextBuilderFactory.createPathTextBuilder();
		boolean fieldIsMandatory = !"false".equalsIgnoreCase(field.getParameters().getWithDefault("MANDATORY"));

		Path elementTargetBasePath = null;
		Path elementSourceBasePath = null;

		if (hasSource) {
			elementSourceBasePath = field.getBinding().getSourcePath().extendPath(new String[] { "element[]" });
		}

		if (hasTarget) {
			elementTargetBasePath = field.getBinding().getTargetPath().extendPath(new String[] { "element[]" });
		}

		int numAddressElements = context.getDataAccessor().count(formatPath);

		for (int i = 1; i <= numAddressElements; ++i) {

			Path elementFormatPath = formatPath.applyIndex(2, i, true);
			String elementName = context.getDataAccessor().get(elementFormatPath.extendPath(new String[] { "name" }));

			String elementLabel = context.getDataAccessor().get(elementFormatPath.extendPath(new String[] { "title" }));

			String elementCodeTableName = context.getDataAccessor()
					.get(elementFormatPath.extendPath(new String[] { "code-table" }));

			boolean elementIsMandatory = !"false".equalsIgnoreCase(
					context.getDataAccessor().get(elementFormatPath.extendPath(new String[] { "mandatory" })));

			boolean elementIsConditionalMandatory = !"false".equalsIgnoreCase(context.getDataAccessor()
					.get(elementFormatPath.extendPath(new String[] { "conditionalMandatory" })));

			clusterBuilder.copy(field);

			this.initAddressField(fBuilder, field, context, elementCodeTableName);

			fBuilder.setParameter("LABEL", context.getDataAccessor()
					.get(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { elementLabel })));
			fBuilder.setParameter("TOOLTIP", context.getDataAccessor()
					.get(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { elementLabel })));
			tBuilder.setPath(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { elementLabel }));
			Text titleText = tBuilder.getText();
			fBuilder.setTitleText(titleText);
			fHeadingBuilder.setStyle(context.getStyle("curam-util-client::field-heading"));
			fHeadingBuilder.setTitleText(titleText);
			fHeadingBuilder.setParameter("MANDATORY", String.valueOf(fieldIsMandatory && elementIsMandatory));
			
			fHeadingBuilder.setParameter("CONDITIONAL_MANDATORY", String.valueOf(elementIsConditionalMandatory));

			if (elementTargetBasePath != null) {
				fBuilder.setTargetPath(elementTargetBasePath.applyIndex(2, elementName, true));

				this.setDefaultParameters(context, fBuilder, elementCodeTableName);
			}

			if (elementSourceBasePath != null) {
				Path elementSourcePath = elementSourceBasePath.applyIndex(2, elementName, true);

				if (elementTargetBasePath == null && elementCodeTableName != null) {

					String elementCodeValue = context.getDataAccessor().get(elementSourcePath);

					if (elementCodeValue != null && !elementCodeValue.equals("")) {
					} else {
						fBuilder.setSourcePath(elementSourcePath);
						fBuilder.setSourcePath(ClientPaths.CODE_TABLE_PATH
								.extendPath(new String[] { elementCodeTableName + "[" + elementCodeValue + "]" }));

					}
				} else {
					fBuilder.setSourcePath(elementSourcePath);

				}
			}

			fBuilder.setParameter("MANDATORY", String.valueOf(fieldIsMandatory && elementIsMandatory));

			fBuilder.setParameter("CONDITIONAL_MANDATORY", String.valueOf(elementIsConditionalMandatory));
			fBuilder.setParameter("STYLE", "address");
			fBuilder.setName(elementName);

			/* BEGIN - CKwong - Task 5155 Customization Address Evidence */

			Field elementField = null;
			if (elementName.equals(countryFieldName)) {
				countryID = "country_" + baseID;

				/*
				 * Add on change event on the country field. Use the hashed fragment id to pass
				 * to on change event. This is necessary as the on change event does not pass
				 * the actual target of the event to the method. In order to determine which
				 * country element is being changed (in the case that there are multiple address
				 * clusters, the countryID will be used to determine the current cluster being
				 * changed
				 */
				//fBuilder.setParameter("ONUNLOAD_ACTION", "rerenderAddressFields(this, \"" + countryID + "\")");

				/*
				 * Get the previous or default value for the country and set the global variable
				 * to pass to initialLoad() javascript method. This prevents the dynamic
				 * rendering from rerendering as a Canadian address if the previous value was
				 * not Canadian.
				 */
				elementField = fBuilder.getComponent();
				countryPosition = i - 1;
				initialCountryValue = context.getFormItemInitialValue(elementField, true, null);

			}

			if (elementField == null)
				elementField = fBuilder.getComponent();
			clusterBuilder.add(fHeadingBuilder.getComponent());
			clusterBuilder.add(elementField);

			/* END - CKwong - Task 5155 Customization Address Evidence */
		}

		clusterBuilder.setStyle(context.getStyle("curam-util-client::inner-cluster-body"));

		clusterBuilder.setParameter("input-cluster", "true");

		clusterBuilder.setParameter("LAYOUT_ORDER", "LABEL");
		clusterBuilder.setParameter("SHOW_LABELS", "true");
		

		return clusterBuilder.getComponent();
	}

	/**
	 * Copy of default address edit renderer method
	 * 
	 * @param context
	 * @param fBuilder
	 * @param elementCodeTableName
	 * @throws DomainException
	 */
	private void setDefaultParameters(RendererContext context, FieldBuilder fBuilder, String elementCodeTableName)
			throws DomainException {
		if (elementCodeTableName != null) {
			//ADO-19904 removed the domain to support read-only fields of address
			//fBuilder.setDomain(context.getDomainForCodeTable(elementCodeTableName));
			fBuilder.setParameter("USE_DEFAULT", "true");
			fBuilder.setParameter("USE_BLANK", "true");
		} else {
			fBuilder.setParameter("USE_DEFAULT", "false");
			fBuilder.setParameter("USE_BLANK", "false");
		}
	}

	/**
	 * Copy of default address edit renderer method
	 * 
	 * @param fBuilder
	 * @param field
	 * @param context
	 * @param elementCodeTableName
	 * @return
	 * @throws DomainException
	 */
	private FieldBuilder initAddressField(FieldBuilder fBuilder, Field field, RendererContext context,
			String elementCodeTableName) throws DomainException {
		fBuilder.copy(field);
		fBuilder.setDomain(context.getDomain("SVR_STRING"));
		if (elementCodeTableName != null) {
			fBuilder.setParameter("extra-wrapper-div-class", "codetable");
		}
		return fBuilder;
	}
}
