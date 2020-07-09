package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.TriggerEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

// ToDo: logging
public final class RuleEngine
{
    @Nonnull
    private final RuleEngineContext ruleEngineContext;

    @Nonnull
    private final List<RuleEvent> ruleEvents;

    @Nullable
    private final RuleEnrollment ruleEnrollment;

    @Nullable
    private TriggerEnvironment triggerEnvironment;

    RuleEngine( @Nonnull RuleEngineContext ruleEngineContext,
        @Nonnull List<RuleEvent> ruleEvents,
        @Nullable RuleEnrollment ruleEnrollment, @Nullable TriggerEnvironment triggerEnvironment )
    {
        this.ruleEngineContext = ruleEngineContext;
        this.ruleEvents = ruleEvents;
        this.ruleEnrollment = ruleEnrollment;
        this.triggerEnvironment = triggerEnvironment;
    }

    @Nonnull
    public List<RuleEvent> events()
    {
        return ruleEvents;
    }

    @Nullable
    public RuleEnrollment enrollment()
    {
        return ruleEnrollment;
    }

    @Nullable
    public TriggerEnvironment triggerEnvironment()
    {
        return triggerEnvironment;
    }

    @Nonnull
    public RuleEngineContext executionContext()
    {
        return ruleEngineContext;
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEvent ruleEvent )
    {
        return evaluate( ruleEvent, ruleEngineContext.rules() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEvent ruleEvent, @Nonnull List<Rule> rulesToEvaluate )
    {
        if ( ruleEvent == null )
        {
            throw new IllegalArgumentException( "ruleEvent == null" );
        }

        Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEvent )
            .ruleVariables( ruleEngineContext.ruleVariables() )
            .ruleEnrollment( ruleEnrollment )
            .triggerEnvironment( triggerEnvironment )
            .ruleEvents( ruleEvents )
            .constantValueMap( ruleEngineContext.constantsValues() )
            .build();

        return new RuleEngineExecution( rulesToEvaluate, valueMap, ruleEngineContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEnrollment ruleEnrollment,
        @Nonnull List<Rule> rulesToEvaluate )
    {
        if ( ruleEnrollment == null )
        {
            throw new IllegalArgumentException( "ruleEnrollment == null" );
        }

        Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEnrollment )
            .ruleVariables( ruleEngineContext.ruleVariables() )
            .triggerEnvironment( triggerEnvironment )
            .ruleEvents( ruleEvents )
            .constantValueMap( ruleEngineContext.constantsValues() )
            .build();

        return new RuleEngineExecution( rulesToEvaluate, valueMap, ruleEngineContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEnrollment ruleEnrollment )
    {
        return evaluate( ruleEnrollment, ruleEngineContext.rules() );
    }

    public static class Builder
    {

        @Nonnull
        private final RuleEngineContext ruleEngineContext;

        @Nullable
        private List<RuleEvent> ruleEvents;

        @Nullable
        private RuleEnrollment ruleEnrollment;

        @Nullable
        private TriggerEnvironment triggerEnvironment;

        Builder( @Nonnull RuleEngineContext ruleEngineContext )
        {
            this.ruleEngineContext = ruleEngineContext;
        }

        @Nonnull
        public Builder events( @Nonnull List<RuleEvent> ruleEvents )
        {
            if ( ruleEvents == null )
            {
                throw new IllegalArgumentException( "ruleEvents == null" );
            }

            this.ruleEvents = Collections.unmodifiableList( new ArrayList<>( ruleEvents ) );
            return this;
        }

        @Nonnull
        public Builder enrollment( @Nonnull RuleEnrollment ruleEnrollment )
        {
            if ( ruleEnrollment == null )
            {
                throw new IllegalArgumentException( "ruleEnrollment == null" );
            }

            this.ruleEnrollment = ruleEnrollment;
            return this;
        }

        @Nonnull
        public Builder triggerEnvironment( @Nonnull TriggerEnvironment triggerEnvironment )
        {
            if ( triggerEnvironment == null )
            {
                throw new IllegalArgumentException( "triggerEnvironment == null" );
            }

            this.triggerEnvironment = triggerEnvironment;
            return this;
        }

        @Nonnull
        public RuleEngine build()
        {
            if ( ruleEvents == null )
            {
                ruleEvents = Collections.unmodifiableList( new ArrayList<RuleEvent>() );
            }

            return new RuleEngine( ruleEngineContext, ruleEvents, ruleEnrollment, triggerEnvironment );
        }
    }
}
