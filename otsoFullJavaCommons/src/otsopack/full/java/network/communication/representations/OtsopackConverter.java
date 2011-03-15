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

import otsopack.commons.data.SemanticFormats;

public class OtsopackConverter extends ConverterHelper {
    private static final VariantInfo VARIANT_TURTLE   = new VariantInfo(MediaType.APPLICATION_RDF_TURTLE);
    private static final VariantInfo VARIANT_NTRIPLES = new VariantInfo(MediaType.TEXT_RDF_NTRIPLES);
    private static final VariantInfo VARIANT_N3       = new VariantInfo(MediaType.TEXT_RDF_N3);
    private static final VariantInfo VARIANT_RDF_XML  = new VariantInfo(MediaType.APPLICATION_RDF_XML);
    private static final VariantInfo VARIANT_RDF_JSON = new VariantInfo(MediaType.APPLICATION_JSON);
    
    private static final Map<VariantInfo, Class<? extends SemanticFormatRepresentation>> VARIANT2CLASS = new HashMap<VariantInfo, Class<? extends SemanticFormatRepresentation>>();
    private static final Map<Class<? extends SemanticFormatRepresentation>, VariantInfo> CLASS2VARIANTS = new HashMap<Class<? extends SemanticFormatRepresentation>, VariantInfo>();
    
    static{
    	VARIANT2CLASS.put(VARIANT_TURTLE,   TurtleRepresentation.class);
    	VARIANT2CLASS.put(VARIANT_NTRIPLES, NTriplesRepresentation.class);
    	VARIANT2CLASS.put(VARIANT_N3,       N3Representation.class);
    	VARIANT2CLASS.put(VARIANT_RDF_XML,  RdfXmlRepresentation.class);
    	VARIANT2CLASS.put(VARIANT_RDF_JSON, RdfJsonRepresentation.class);
    	
    	for(VariantInfo variantInfo : VARIANT2CLASS.keySet())
    		CLASS2VARIANTS.put(VARIANT2CLASS.get(variantInfo), variantInfo);

    	checkDefaultFormats();
    }
    
    private static void checkDefaultFormats(){
    	final List<String> formats = new Vector<String>();
    	for(VariantInfo variantInfo : VARIANT2CLASS.keySet()){
    		final String semanticFormat = SemanticFormatRepresentationRegistry.getSemanticFormat(variantInfo.getMediaType());
    		if(semanticFormat == null)
    			System.err.println("WARNING: " + variantInfo.getMediaType().getName() + " registered in " + OtsopackConverter.class.getName() + " but not in " + SemanticFormatRepresentationRegistry.class.getName());
    		else
    			formats.add(semanticFormat);
    	}
    	
    	for(String format : SemanticFormats.getSemanticFormats())
    		if(!formats.contains(format))
    			System.err.println("WARNING: Format " + format + " not registered in " + OtsopackConverter.class.getName());
    }
    

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        for(VariantInfo variantInfo : VARIANT2CLASS.keySet()){
        	if(variantInfo.isCompatible(source)){
        		result = addObjectClass(result, Object.class);
                result = addObjectClass(result, VARIANT2CLASS.get(variantInfo));
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

    @Override
    public <T> void updatePreferences(List<Preference<MediaType>> preferences,
            Class<T> entity) {
    	for(VariantInfo variantInfo : VARIANT2CLASS.keySet())
    		updatePreferences(preferences, variantInfo.getMediaType(), 1.0F);
    }

}
