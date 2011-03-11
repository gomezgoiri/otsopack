package otsopack.full.java.network.communication.representations;

import java.io.IOException;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.UniformResource;

public class OtsopackConverter extends ConverterHelper {
    private static final VariantInfo VARIANT_TURTLE   = new VariantInfo(TurtleRepresentation.TEXT_RDF_TURTLE);
    private static final VariantInfo VARIANT_NTRIPLES = new VariantInfo(MediaType.TEXT_RDF_NTRIPLES);
    private static final VariantInfo VARIANT_N3       = new VariantInfo(MediaType.TEXT_RDF_N3);

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        if (VARIANT_TURTLE.isCompatible(source)) {
            result = addObjectClass(result, Object.class);
            result = addObjectClass(result, TurtleRepresentation.class);
        }else if(VARIANT_NTRIPLES.isCompatible(source)) {
            result = addObjectClass(result, Object.class);
            result = addObjectClass(result, NTriplesRepresentation.class);
        }else if(VARIANT_N3.isCompatible(source)) {
            result = addObjectClass(result, Object.class);
            result = addObjectClass(result, N3Representation.class);
        }

        return result;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;

        if (source != null) {
        	if(TurtleRepresentation.class.isAssignableFrom(source))
        		result = addVariant(result, VARIANT_TURTLE);
        	if(NTriplesRepresentation.class.isAssignableFrom(source))
        		result = addVariant(result, VARIANT_NTRIPLES);
        	if(N3Representation.class.isAssignableFrom(source))
        		result = addVariant(result, VARIANT_N3);
        }

        return result;
    }

    @Override
    public float score(Object source, Variant target, UniformResource resource) {
        float result = -1.0F;

        if (source instanceof TurtleRepresentation || source instanceof NTriplesRepresentation || source instanceof N3Representation) {
            result = 1.0F;
        } else {
            if (target == null) {
                result = 0.5F;
            } else if (VARIANT_TURTLE.isCompatible(target)) {
                result = 0.8F;
            } else if (VARIANT_N3.isCompatible(target)) {
                result = 0.8F;
            } else if (VARIANT_NTRIPLES.isCompatible(target)) {
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

        if (source instanceof TurtleRepresentation) {
            result = 1.0F;
        } else if(source instanceof N3Representation) {
            result = 1.0F;
        } else if(source instanceof NTriplesRepresentation) {
            result = 1.0F;
        } else if ((target != null)
        		&& (TurtleRepresentation.class.isAssignableFrom(target)
            			|| N3Representation.class.isAssignableFrom(target)
            			|| NTriplesRepresentation.class.isAssignableFrom(target)
        				)) {
            result = 1.0F;
        } else if (VARIANT_TURTLE.isCompatible(source)) {
            result = 0.8F;
        } else if (VARIANT_N3.isCompatible(source)) {
            result = 0.8F;
        } else if (VARIANT_NTRIPLES.isCompatible(source)) {
            result = 0.8F;
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(Representation source, Class<T> target, UniformResource resource) throws IOException {
        Object result = null;

        Representation representation = null;

        if (source instanceof TurtleRepresentation) {
            representation = source;
        } else if (source instanceof N3Representation) {
            representation = source;
        } else if (source instanceof NTriplesRepresentation) {
            representation = source;
        } else if (VARIANT_TURTLE.isCompatible(source)) {
            representation = new TurtleRepresentation(source);
        } else if (VARIANT_N3.isCompatible(source)) {
            representation = new N3Representation(source);
        } else if (VARIANT_NTRIPLES.isCompatible(source)) {
            representation = new NTriplesRepresentation(source);
        }

        if (representation != null) {
            // Handle the conversion
            if ((target != null)
                    && (TurtleRepresentation.class.isAssignableFrom(target)
                    		|| N3Representation.class.isAssignableFrom(target)
                    		|| NTriplesRepresentation.class.isAssignableFrom(target)
                    		)) {
                result = representation;
            } else {
            	throw new IllegalStateException("ESTO NO DEBERÍA SER ASÍ");
            }
        }

        return (T) result;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target,
            UniformResource resource) {
        Representation result = null;

        if (source instanceof TurtleRepresentation) {
            result = (TurtleRepresentation) source;
        } else if(source instanceof N3Representation){
        	result = (N3Representation) source;
        } else if(source instanceof NTriplesRepresentation){
        	result = (NTriplesRepresentation) source;
        } 

        return result;
    }

    @Override
    public <T> void updatePreferences(List<Preference<MediaType>> preferences,
            Class<T> entity) {
        updatePreferences(preferences, TurtleRepresentation.TEXT_RDF_TURTLE, 1.0F);
        updatePreferences(preferences, MediaType.TEXT_RDF_N3, 1.0F);
        updatePreferences(preferences, MediaType.TEXT_RDF_NTRIPLES, 1.0F);
    }

}
