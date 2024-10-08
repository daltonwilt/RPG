package com.outcast.rpg.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.character.attribute.AttributeTypeRegistry;
import com.outcast.rpg.config.attribute.AttributesConfig;
import com.outcast.rpg.service.character.AttributeService;
import com.outcast.rpg.util.expression.ClampFunction;
import com.udojava.evalex.Expression;
import org.bukkit.entity.LivingEntity;
import org.javatuples.Pair;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ExpressionService {

    @Inject
    private AttributesConfig attributesConfig;

    @Inject
    private AttributeService attributeService;
    @Inject
    private AttributeTypeRegistry attributeTypeRegistry;

    private final Map<String, Expression> cachedExpression = new HashMap<>();

    // Map for attribute variable strings; SOURCE, TARGET
    private Map<AttributeType, Pair<String, String>> attributeVariables;

    public void init() {
        this.attributeVariables = new HashMap<>();

        for(AttributeType type : attributeTypeRegistry.getAll()) {
            String source = "SOURCE_" + type.getShortName().toUpperCase();
            String target = "TARGET_" + type.getShortName().toUpperCase();

            attributeVariables.put(type, new Pair<>(source, target));
        }
    }

    public Expression getExpression(String expression) {
        Expression result = cachedExpression.get(expression);

        if(result == null) {
            result = new Expression(expression);
            result.addFunction(new ClampFunction());
            cachedExpression.put(expression, result);
        }

        return result;
    }

    public void populateAttributes(Expression expression, Map<AttributeType, Double> attributes, String name) {
        String pattern = name.toUpperCase() + "_%s";
        attributes.forEach((type, value) -> expression.setVariable(
                String.format(pattern, type.getShortName().toUpperCase()),
                BigDecimal.valueOf(value)
        ));
    }

    public void populateSourceAttributes(Expression expression, Map<AttributeType, Double> attributes) {
        attributes.forEach((type, value) -> expression.setVariable(
                attributeVariables.get(type).getValue0(),
                BigDecimal.valueOf(value)
        ));
    }

    public void populateTargetAttributes(Expression expression, Map<AttributeType, Double> attributes) {
        attributes.forEach((type, value) -> expression.setVariable(
                attributeVariables.get(type).getValue1(),
                BigDecimal.valueOf(value)
        ));
    }

    public BigDecimal evalExpression(LivingEntity source, String stringExpression) {
        return evalExpression(source, getExpression(stringExpression));
    }

    public BigDecimal evalExpression(LivingEntity source, LivingEntity target, String stringExpression) {
        return evalExpression(source, target, getExpression(stringExpression));
    }

    public BigDecimal evalExpression(LivingEntity source, Expression expression) {
        populateSourceAttributes(expression, attributeService.getAllAttributes(source));
        return expression.eval(true);
    }

    public BigDecimal evalExpression(LivingEntity source, LivingEntity target, Expression expression) {
        populateSourceAttributes(expression, attributeService.getAllAttributes(source));
        populateTargetAttributes(expression, attributeService.getAllAttributes(target));
        return expression.eval(true);
    }

}
