package curam.ca.bdm.widget.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import curam.core.struct.AddressMapList;
import curam.omega3.config.appconfig.AddressFormat;
import curam.omega3.taglib.formwidget.CuramAddress;
import curam.util.client.ClientException;
import curam.util.client.domain.render.view.AbstractViewRenderer;
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
import curam.util.common.domain.Domain;
import curam.util.common.domain.DomainException;
import curam.util.common.path.DataAccessException;
import curam.util.common.path.DataAccessor;
import curam.util.common.path.Path;
import curam.util.common.plugin.PlugInException;
import curam.util.render.address.AddressViewRenderer;
import curam.util.render.address.DefaultAddressEditRenderer;

public class BDMAddressViewRender extends AddressViewRenderer {

	@Override
	public void render(Field field, DocumentFragment fragment, RendererContext context, RendererContract contract)
			throws ClientException, DataAccessException, PlugInException {
		String addressFormatName;
		Path formatPath = ClientPaths.ADDRESS_FORMAT_PATH.extendPath(new String[] { BDMAddressContants.kAddressFormatArray });
		DocumentFragment fieldFragment = fragment.getOwnerDocument().createDocumentFragment();

		boolean hasSource = (field.getBinding().getSourcePath() != null);
		boolean hasTarget = (field.getBinding().getTargetPath() != null);

		if (hasSource) {
			addressFormatName = context.getDataAccessor()
					.get(field.getBinding().getSourcePath().extendPath(new String[] { BDMAddressContants.kFormat }));
		} else {
			addressFormatName = context.getDataAccessor()
					.get(ClientPaths.ADDRESS_FORMAT_PATH.extendPath(new String[] { BDMAddressContants.kDefaultFormatName }));
		}
		formatPath = formatPath.applyIndex(2, addressFormatName, false);

		Element addressDiv = fragment.getOwnerDocument().createElement(BDMAddressContants.kDiv);
		addressDiv.setAttribute(BDMAddressContants.kClass, BDMAddressContants.kBxaddress);

		// createAddressHeadingDescription(context, fragment, addressDiv);

		Container innerClusterBody = createAddressFieldCluster(context, field, formatPath, hasTarget, hasSource);

		context.render((Component) innerClusterBody, fieldFragment, contract.createSubcontract());

		addressDiv.appendChild(fieldFragment);
		fragment.appendChild(addressDiv);
	}

	private Container createAddressFieldCluster(RendererContext context, Field field, Path formatPath,
			boolean hasTarget, boolean hasSource) throws ClientException, DataAccessException, PlugInException {

		ContainerBuilder clusterBuilder = ComponentBuilderFactory.createContainerBuilder();
		ComponentBuilder<Component> fHeadingBuilder = ComponentBuilderFactory.createComponentBuilder();
		FieldBuilder fBuilder = ComponentBuilderFactory.createFieldBuilder();
		PathTextBuilder tBuilder = TextBuilderFactory.createPathTextBuilder();
		boolean fieldIsMandatory = !BDMAddressContants.kFalse.equalsIgnoreCase(field.getParameters().getWithDefault(BDMAddressContants.kMANDATORY));
		Path elementTargetBasePath = null;
		Path elementSourceBasePath = null;

		if (hasSource)
			elementSourceBasePath = field.getBinding().getSourcePath().extendPath(new String[] { BDMAddressContants.kElement });

		if (hasTarget)
			elementTargetBasePath = field.getBinding().getTargetPath().extendPath(new String[] { BDMAddressContants.kElement });

		int numAddressElements = context.getDataAccessor().count(formatPath);

		
		final Map<String, String> addressAddressData = addressAddressData(field, context);
		final String country = addressAddressData.get(BDMAddressContants.kCOUNTRY);
		String isUnparse = addressAddressData.get(BDMAddressContants.kBDMUNPARSE);
		// Special Case for International Address
		if(!country.equals(BDMAddressContants.kCA) && !country.equals(BDMAddressContants.kUS)) {
			isUnparse = BDMAddressContants.kisUnparseInd;
		}
		
		//BEGIN: Task 93506: DEV: Address Format Updates
		final HashMap<String, String> unParedAddressLabels = getUnParedAddressLabels(context);
		//END: Task 93506: DEV: Address Format Updates
		
		for (int i = 1; i <= numAddressElements; i++) {
			Path elementFormatPath = formatPath.applyIndex(2, i, true);
			String elementName = context.getDataAccessor().get(elementFormatPath.extendPath(new String[] { BDMAddressContants.kName }));
			
			Boolean isCAAddress = country.equals(BDMAddressContants.kCA)
					&& getCAAddressElements().contains(elementName) && (isUnparse.isEmpty() || isUnparse.equals(BDMAddressContants.kisNotUnparseInd));
			Boolean isUSAddress = country.equals(BDMAddressContants.kUS)
					&& getUSAddressElements().contains(elementName) && (isUnparse.isEmpty() || isUnparse.equals(BDMAddressContants.kisNotUnparseInd));
			Boolean isIntlAddress = !country.equals(BDMAddressContants.kCA) && !country.equals(BDMAddressContants.kUS)
					&& getInternationaAddressElements().contains(elementName)
					&& (isUnparse.isEmpty() || isUnparse.equals(BDMAddressContants.kisNotUnparseInd));
			Boolean isCAUnparsedAddress = country.equals(BDMAddressContants.kCA)
					&& getCAUnparsedAddressElements().contains(elementName) && !isUnparse.isEmpty()
					&& isUnparse.equals(BDMAddressContants.kisUnparseInd);
			Boolean isIntlUnparsedAddress = !country.equals(BDMAddressContants.kCA)
					&& getInternationalUnparsedAddressElements().contains(elementName) && !isUnparse.isEmpty()
					&& isUnparse.equals(BDMAddressContants.kisUnparseInd);
			
			if (isCAAddress || isUSAddress || isIntlAddress || isCAUnparsedAddress || isIntlUnparsedAddress) {
				
				String elementLabel = context.getDataAccessor()
						.get(elementFormatPath.extendPath(new String[] { BDMAddressContants.kTitle }));
				
				if(isCAUnparsedAddress.booleanValue() && elementName.equals(BDMAddressContants.kPROV)) {
					elementLabel = BDMAddressContants.kUNPARSEDCAPROVLABEL;
				}
				
				String elementCodeTableName = context.getDataAccessor()
						.get(elementFormatPath.extendPath(new String[] { BDMAddressContants.kCode_table }));
				boolean elementIsMandatory = !BDMAddressContants.kFalse.equalsIgnoreCase(
						context.getDataAccessor().get(elementFormatPath.extendPath(new String[] { BDMAddressContants.kMandatory })));
				boolean elementIsConditionalMandatory = !BDMAddressContants.kFalse.equalsIgnoreCase(context.getDataAccessor()
						.get(elementFormatPath.extendPath(new String[] { BDMAddressContants.kConditionalMandatory })));

				clusterBuilder.copy((Component) field);
				initAddressField(fBuilder, field, context, elementCodeTableName);

				//BEGIN: Task 93506: DEV: Address Format Updates
				if(!isUnparse.isEmpty() && isUnparse.equals(BDMAddressContants.kisUnparseInd) && unParedAddressLabels.containsKey(elementName)) {
					
					fBuilder.setParameter(BDMAddressContants.kLABEL, context.getDataAccessor()
							.get(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { unParedAddressLabels.get(elementName) })));

					fBuilder.setParameter(BDMAddressContants.kTOOLTIP, context.getDataAccessor()
							.get(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { unParedAddressLabels.get(elementName) })));

					tBuilder.setPath(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { unParedAddressLabels.get(elementName) }));					
				} else {
					
				fBuilder.setParameter(BDMAddressContants.kLABEL, context.getDataAccessor()
						.get(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { elementLabel })));

				fBuilder.setParameter(BDMAddressContants.kTOOLTIP, context.getDataAccessor()
						.get(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { elementLabel })));

				tBuilder.setPath(ClientPaths.CDEJ_RESOURCES_PATH.extendPath(new String[] { elementLabel }));
				
				}
				//END: Task 93506: DEV: Address Format Updates
				
				Text titleText = tBuilder.getText();
				fBuilder.setTitleText(titleText);
				fHeadingBuilder.setStyle(context.getStyle(BDMAddressContants.kCuramUtilClientFieldHeading));
				fHeadingBuilder.setTitleText(titleText);
				fHeadingBuilder.setParameter(BDMAddressContants.kMANDATORY, String.valueOf((fieldIsMandatory && elementIsMandatory)));
				fHeadingBuilder.setParameter(BDMAddressContants.kCONDITIONALMANDATORY, String.valueOf(elementIsConditionalMandatory));

				if (elementTargetBasePath != null) {
					fBuilder.setTargetPath(elementTargetBasePath.applyIndex(2, elementName, true));
					setDefaultParameters(context, fBuilder, elementCodeTableName);

				}
				if (elementSourceBasePath != null) {
					Path elementSourcePath = elementSourceBasePath.applyIndex(2, elementName, true);

					if (elementTargetBasePath == null && elementCodeTableName != null) {

						String elementCodeValue = context.getDataAccessor().get(elementSourcePath);

						if (elementCodeValue == null || elementCodeValue.equals(BDMAddressContants.kEmpty)) {
							fBuilder.setSourcePath(elementSourcePath);

						} else {
							fBuilder.setSourcePath(ClientPaths.CODE_TABLE_PATH
									.extendPath(new String[] { elementCodeTableName + BDMAddressContants.kOpenSquareBracket + elementCodeValue + BDMAddressContants.kCloseSquareBracket }));

						}
					} else {
						fBuilder.setSourcePath(elementSourcePath);

					}
				}
				fBuilder.setParameter(BDMAddressContants.kMANDATORY, String.valueOf((fieldIsMandatory && elementIsMandatory)));
				fBuilder.setParameter(BDMAddressContants.kCONDITIONALMANDATORY, String.valueOf(elementIsConditionalMandatory));

				fBuilder.setParameter(BDMAddressContants.kSTYLE, BDMAddressContants.kAddress);

				clusterBuilder.add(fHeadingBuilder.getComponent());
				clusterBuilder.add(fBuilder.getComponent());
			}

		}
		clusterBuilder.setStyle(context.getStyle(BDMAddressContants.kCuramUtilClientInnerClusterBody));

		clusterBuilder.setParameter(BDMAddressContants.kInputCluster, BDMAddressContants.kTrue);

		clusterBuilder.setParameter(BDMAddressContants.kLAYOUTORDER, BDMAddressContants.kLABEL);
		clusterBuilder.setParameter(BDMAddressContants.kSHOWLABELS, BDMAddressContants.kTrue);

		return (Container) clusterBuilder.getComponent();
	}

	private void setDefaultParameters(RendererContext context, FieldBuilder fBuilder, String elementCodeTableName)
			throws DomainException {
		if (elementCodeTableName != null) {

			fBuilder.setDomain((Domain) context.getDomainForCodeTable(elementCodeTableName));

			fBuilder.setParameter(BDMAddressContants.kUSEDEFAULT, BDMAddressContants.kTrue);
			fBuilder.setParameter(BDMAddressContants.kUSEBLANK, BDMAddressContants.kTrue);
		} else {
			fBuilder.setParameter(BDMAddressContants.kUSEDEFAULT, BDMAddressContants.kFalse);
			fBuilder.setParameter(BDMAddressContants.kUSEBLANK, BDMAddressContants.kFalse);
		}
	}

	private FieldBuilder initAddressField(FieldBuilder fBuilder, Field field, RendererContext context,
			String elementCodeTableName) throws DomainException {
		fBuilder.copy((Component) field);
		fBuilder.setDomain((Domain) context.getDomain(BDMAddressContants.kSVRSTRING));

		if (elementCodeTableName != null)
			fBuilder.setParameter(BDMAddressContants.kExtraWrapperDivClass, BDMAddressContants.kCodetable);

		return fBuilder;
	}
	
	private Map<String, String> addressAddressData(Field field, RendererContext context) throws DataAccessException {
		
		 String addressData = context.getDataAccessor()
    	       .get(field.getBinding().getSourcePath());
				 
		 String[] addressElementDataList = addressData.split(BDMAddressContants.kAddressDataSplit);
		 
		    Map<String, String> addressDataMap = new HashMap<String, String>();
		    
		    for(String addressElementData : addressElementDataList) {
		    	if(addressElementData.contains(BDMAddressContants.kEquals)) {
			    	String[] eachAddressData = addressElementData.split(BDMAddressContants.kEquals);
			    	
			    	if(eachAddressData.length == 1) {
				    	String addressKey = eachAddressData[0];
				    	addressDataMap.put(addressKey, BDMAddressContants.kEmpty);
			    	} else {				    	String addressKey = eachAddressData[0];
				    	String addressValue = eachAddressData[1];				    	addressDataMap.put(addressKey, addressValue);		    		
		    	}
	    	}
	    }
		 
		return addressDataMap;
	}
	
	private ArrayList<String> getCAAddressElements(){
		
		final ArrayList<String> caAddressElements = new ArrayList<String>();
		caAddressElements.add(BDMAddressContants.kCOUNTRY);
		caAddressElements.add(BDMAddressContants.kPOSTCODE);
		caAddressElements.add(BDMAddressContants.kAPT);
		caAddressElements.add(BDMAddressContants.kPOBOXNO);
		caAddressElements.add(BDMAddressContants.kADD1);
		caAddressElements.add(BDMAddressContants.kADD2);
		caAddressElements.add(BDMAddressContants.kCITY);
		caAddressElements.add(BDMAddressContants.kPROV);		
		return caAddressElements;
	}
	
	private ArrayList<String> getUSAddressElements(){
		
		final ArrayList<String> usAddressElements = new ArrayList<String>();
		usAddressElements.add(BDMAddressContants.kCOUNTRY);
		usAddressElements.add(BDMAddressContants.kZIP);
		usAddressElements.add(BDMAddressContants.kAPT);
		usAddressElements.add(BDMAddressContants.kPOBOXNO);
		usAddressElements.add(BDMAddressContants.kADD1);
		usAddressElements.add(BDMAddressContants.kADD2);
		usAddressElements.add(BDMAddressContants.kCITY);
		usAddressElements.add(BDMAddressContants.kSTATEPROV);		
		return usAddressElements;
	}	
	
	private ArrayList<String> getInternationaAddressElements(){
		
		final ArrayList<String> internationaAddressElements = new ArrayList<String>();
		internationaAddressElements.add(BDMAddressContants.kCOUNTRY);
		internationaAddressElements.add(BDMAddressContants.kBDMZIPX);
		internationaAddressElements.add(BDMAddressContants.kAPT);
		internationaAddressElements.add(BDMAddressContants.kPOBOXNO);
		internationaAddressElements.add(BDMAddressContants.kADD1);
		internationaAddressElements.add(BDMAddressContants.kADD2);
		internationaAddressElements.add(BDMAddressContants.kCITY);
		internationaAddressElements.add(BDMAddressContants.kBDMSTPROVX);		
		return internationaAddressElements;
	}		

	
	//START: Task 93506: DEV: Address Format Updates
	private ArrayList<String> getCAUnparsedAddressElements(){
		
		final ArrayList<String> caUnparsedAddressElements = new ArrayList<String>();
		caUnparsedAddressElements.add(BDMAddressContants.kCOUNTRY);
		caUnparsedAddressElements.add(BDMAddressContants.kAPT);
		caUnparsedAddressElements.add(BDMAddressContants.kADD1);
		caUnparsedAddressElements.add(BDMAddressContants.kADD2);
		caUnparsedAddressElements.add(BDMAddressContants.kCITY);
		caUnparsedAddressElements.add(BDMAddressContants.kPROV);
		caUnparsedAddressElements.add(BDMAddressContants.kPOSTCODE);
		return caUnparsedAddressElements;
	}	
	
	private ArrayList<String> getInternationalUnparsedAddressElements(){
		
		final ArrayList<String> internationaUnparsedAddressElements = new ArrayList<String>();
		internationaUnparsedAddressElements.add(BDMAddressContants.kCOUNTRY);
		internationaUnparsedAddressElements.add(BDMAddressContants.kAPT);
		internationaUnparsedAddressElements.add(BDMAddressContants.kADD1);
		internationaUnparsedAddressElements.add(BDMAddressContants.kADD2);
		internationaUnparsedAddressElements.add(BDMAddressContants.kCITY);	
		return internationaUnparsedAddressElements;
	}		
	
	private HashMap<String, String> getUnParedAddressLabels(final RendererContext context) throws DataAccessException {

		HashMap<String, String> unParedAddressLabels = new HashMap<String, String>();

		unParedAddressLabels.put(BDMAddressContants.kAPT, BDMAddressContants.kAddressLine1);
		unParedAddressLabels.put(BDMAddressContants.kADD1, BDMAddressContants.kAddressLine2);
		unParedAddressLabels.put(BDMAddressContants.kADD2, BDMAddressContants.kAddressLine3);
		unParedAddressLabels.put(BDMAddressContants.kCITY, BDMAddressContants.kAddressLine4);
		return unParedAddressLabels;
	}

	private Boolean isAddressEleementForDisplay(final String country, final String isUnparse,
			final String elementName) {

		Boolean isAddressEleementForDisplay = false;

		if (isUnparse.isEmpty()) {

			if (country.equals(BDMAddressContants.kCA) && getCAAddressElements().contains(elementName)
					|| country.equals(BDMAddressContants.kUS) && getUSAddressElements().contains(elementName) || !country.equals(BDMAddressContants.kCA)
							&& !country.equals(BDMAddressContants.kUS) && getInternationaAddressElements().contains(elementName)) {
				isAddressEleementForDisplay = true;
			}

		} else if (!isUnparse.isEmpty() && isUnparse.equals(BDMAddressContants.kisUnparseInd)) {

			if (country.equals(BDMAddressContants.kCA) && getCAUnparsedAddressElements().contains(elementName)
					|| !country.equals(BDMAddressContants.kCA) && getInternationalUnparsedAddressElements().contains(elementName)) {
				isAddressEleementForDisplay = true;
			}
		}
		return isAddressEleementForDisplay;
	}	
	//END: Task 93506: DEV: Address Format Updates
}
