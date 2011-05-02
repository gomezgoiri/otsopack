package otsopack.full.java.network.communication.representations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.UniformResource;

import otsopack.commons.data.SemanticFormat;

public class OtsopackConverter extends ConverterHelper {
	
	public static final String MEDIA_TYPE_RDF_XML   = "rdf+xml";
	public static final String MEDIA_TYPE_TURTLE    = "turtle";
	public static final String MEDIA_TYPE_NTRIPLES  = "nt";
	public static final String MEDIA_TYPE_ACROSS_MULTIPART = "across/multipart";
	
	public static final MediaType ACROSS_MULTIPART_MEDIA_TYPE = MediaType.register(MEDIA_TYPE_ACROSS_MULTIPART, "Across multipart document");
	
	// Using "|" as required by RESTlet; n3 and json are already defined in the MetadataService 
	public static final String MEDIA_TYPE_SEMANTIC_FORMATS = MEDIA_TYPE_NTRIPLES + "|n3|json|" + MEDIA_TYPE_RDF_XML + "|" + MEDIA_TYPE_TURTLE + "|" + MEDIA_TYPE_ACROSS_MULTIPART;

	

    private static final VariantInfo VARIANT_TURTLE           = new VariantInfo(MediaType.APPLICATION_RDF_TURTLE);
    private static final VariantInfo VARIANT_NTRIPLES         = new VariantInfo(MediaType.TEXT_RDF_NTRIPLES);
    private static final VariantInfo VARIANT_N3               = new VariantInfo(MediaType.TEXT_RDF_N3);
    private static final VariantInfo VARIANT_RDF_XML          = new VariantInfo(MediaType.APPLICATION_RDF_XML);
    private static final VariantInfo VARIANT_RDF_JSON         = new VariantInfo(MediaType.APPLICATION_JSON);
    private static final VariantInfo VARIANT_ACROSS_MULTIPART = new VariantInfo(ACROSS_MULTIPART_MEDIA_TYPE);
    
    private static final Map<VariantInfo, Class<? extends SemanticFormatRepresentation>> VARIANT2CLASS = new HashMap<VariantInfo, Class<? extends SemanticFormatRepresentation>>();
    private static final Map<Class<? extends SemanticFormatRepresentation>, VariantInfo> CLASS2VARIANTS = new HashMap<Class<? extends SemanticFormatRepresentation>, VariantInfo>();
    
    private final static ThreadLocal<MediaType []> ENABLED_VARIANTS = new ThreadLocal<MediaType []>();
    
    static{
    	VARIANT2CLASS.put(VARIANT_TURTLE,           TurtleRepresentation.class);
    	VARIANT2CLASS.put(VARIANT_NTRIPLES,         NTriplesRepresentation.class);
    	VARIANT2CLASS.put(VARIANT_N3,               N3Representation.class);
    	VARIANT2CLASS.put(VARIANT_RDF_XML,          RdfXmlRepresentation.class);
    	VARIANT2CLASS.put(VARIANT_RDF_JSON,         RdfJsonRepresentation.class);
    	VARIANT2CLASS.put(VARIANT_ACROSS_MULTIPART, RdfMultipartRepresentation.class);
    	
    	for(VariantInfo variantInfo : VARIANT2CLASS.keySet())
    		CLASS2VARIANTS.put(VARIANT2CLASS.get(variantInfo), variantInfo);

    	checkDefaultFormats();
    }
    
    private static void checkDefaultFormats(){
    	final List<SemanticFormat> formats = new Vector<SemanticFormat>();
    	for(VariantInfo variantInfo : VARIANT2CLASS.keySet()){
    		final SemanticFormat semanticFormat = SemanticFormatRepresentationRegistry.getSemanticFormat(variantInfo.getMediaType());
    		if(semanticFormat == null)
    			System.err.println("WARNING: " + variantInfo.getMediaType().getName() + " registered in " + OtsopackConverter.class.getName() + " but not in " + SemanticFormatRepresentationRegistry.class.getName());
    		else
    			formats.add(semanticFormat);
    	}
    	
    	for(SemanticFormat format : SemanticFormat.getSemanticFormats())
    		if(!formats.contains(format))
    			System.err.println("WARNING: Format " + format + " not registered in " + OtsopackConverter.class.getName());
    	
    	if(MEDIA_TYPE_SEMANTIC_FORMATS.split("|").length != VARIANT2CLASS.size())
    		System.err.println("WARNING: Media type semantic formats size is different of VARIANT2CLASS size at " + OtsopackConverter.class.getName());
    }
    

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        for(VariantInfo variantInfo : VARIANT2CLASS.keySet()){
        	if(variantInfo.isCompatible(source)){
        		result = addObjectClass(result, Object.class);
                result = addObjectClass(result, VARIANT2CLASS.get(variantInfo));
                break;
        	}
        }

        return result;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;

        if (source != null) 
        	for(Class<? extends SemanticFormatRepresentation> klass : CLASS2VARIANTS.keySet())
        		if(klass.isAssignableFrom(source))
        			result = addVariant(result, CLASS2VARIANTS.get(klass));

        return result;
    }
    
    private boolean isAssignable(Object source){
    	return isAssignable(source.getClass());
    }
    
    private boolean isAssignable(Class<?> target){
    	if(target.equals(SemanticFormatRepresentation.class))
    		return true;
    	for(Class<? extends SemanticFormatRepresentation> klass : CLASS2VARIANTS.keySet())
    		if(klass.isAssignableFrom(target))
    			return true;
    	return false;
    }
    
    private boolean isCompatible(Variant target){
    	for(VariantInfo variantInfo : VARIANT2CLASS.keySet())
    		if(variantInfo.isCompatible(target))
    			return true;
    	return false;
    }
    
    private Representation buildRepresentation(Representation source){
    	for(VariantInfo variantInfo : VARIANT2CLASS.keySet())
    		if(variantInfo.isCompatible(source))
    			try{
    				return VARIANT2CLASS.get(variantInfo).getDeclaredConstructor(Representation.class).newInstance(source);
    			}catch(Exception e){
    				System.err.println("Couldn't build representation: " + e.getMessage());
    				e.printStackTrace();
    			}
    	return null;
    }

    @Override
    public float score(Object source, Variant target, UniformResource resource) {
        float result = -1.0F;

        if (isAssignable(source)) {
            result = 1.0F;
        } else {
            if (target == null) {
                result = 0.5F;
            } else if (isCompatible(target)) {
                result = 0.8F;
            } else {
                result = 0.5F;
            }
        }

        return result;
    }

    @Override
    public <T> float score(Representation source, Class<T> target,
            UniformResource resource) {
        float result = -1.0F;

        if (isAssignable(source)) {
            result = 1.0F;
        } else if (target != null && isAssignable(target)) {
            result = 1.0F;
        } else if (isCompatible(source)) {
            result = 0.8F;
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(Representation source, Class<T> target, UniformResource resource) throws IOException {
        Object result = null;

        Representation representation = null;

        if (isAssignable(source))
            representation = source;
        else 
        	representation = buildRepresentation(source);

        if (representation != null) {
            // Handle the conversion
            if (target != null && isAssignable(target)) {
                result = representation;
            }
        }

        return (T) result;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target,
            UniformResource resource) {
        Representation result = null;

        if(isAssignable(source))
        	result = (Representation) source;

        return result;
    }
    
    public static void setEnabledVariants(MediaType ... mediaTypes){
    	ENABLED_VARIANTS.set(mediaTypes);
    }

    @Override
    public <T> void updatePreferences(List<Preference<MediaType>> preferences, Class<T> entity) {
    	for(VariantInfo variantInfo : VARIANT2CLASS.keySet()){
    		final MediaType [] mediaTypes = ENABLED_VARIANTS.get();
    		if(mediaTypes == null)
    			updatePreferences(preferences, variantInfo.getMediaType(), 1.0F);
    		else{
    			for(MediaType enabledMediaType : mediaTypes)
    				if(variantInfo.getMediaType().equals(enabledMediaType)){
    					updatePreferences(preferences, variantInfo.getMediaType(), 1.0F);
    					break;
    				}
    		}
    	}
    }
    
}
