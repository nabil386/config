package curam.ca.gc.bdm.sl.infrastructure.assessment.impl;

import curam.ca.gc.bdm.message.BDMDETERMINATIONINTERVALSUMMARIZERS;
import curam.codetable.impl.RULESTAGTYPEEntry;
import curam.core.sl.fact.TabDetailFormatterFactory;
import curam.core.sl.infrastructure.assessment.codetable.impl.CASEDETERMINATIONINTERVALRESULTEntry;
import curam.core.sl.infrastructure.assessment.impl.CREOLECaseDeterminationAccessor;
import curam.core.sl.infrastructure.assessment.impl.Determination;
import curam.core.sl.infrastructure.assessment.impl.DeterminationInterval;
import curam.core.sl.infrastructure.assessment.impl.DeterminationIntervalObjective;
import curam.core.sl.infrastructure.assessment.impl.DeterminationIntervalSummarizerStrategy;
import curam.core.sl.infrastructure.assessment.impl.DeterminationIntervalTag;
import curam.core.sl.infrastructure.assessment.impl.TagType;
import curam.core.sl.struct.AmountDetail;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.transaction.TransactionInfo;
import curam.util.type.DateRange;
import curam.util.type.FrequencyPattern.PatternType;
import curam.util.type.Money;
import java.util.Iterator;

/**
 *
 * This class implements the logic for when the determination is created for the
 * dependant benefit PDC. It creates a summary of a One Time payment of $500 if
 * eligible.
 *
 * @author alim.maredia
 *
 */
public class TotalOneTimeMonetaryEntitlementDeterminationIntervalSummarizerStrategy
  implements DeterminationIntervalSummarizerStrategy {

  /**
   * This method gets the summary of a determination
   *
   * @return String
   * @param creoleCaseDetermination
   * @param intervalDateRange
   * @throws AppException
   * @throws InformationalException
   *
   */
  @Override
  public String getSummary(
    final CREOLECaseDeterminationAccessor creoleCaseDetermination,
    final DateRange intervalDateRange)
    throws AppException, InformationalException {

    // Check if the individual is eligible for this determination
    final Determination determination =
      creoleCaseDetermination.getDeterminationResult();
    final DeterminationInterval determinationInterval = determination
      .eligibilityEntitlementTimeline().valueOn(intervalDateRange.start());
    if (!determinationInterval.result()
      .equals(CASEDETERMINATIONINTERVALRESULTEntry.ELIGIBLE)) {
      return "";
    } else {
      // Create the summary message
      double indicativeAmount = 0.0D;

      double tagOneTimeAmount;
      for (final Iterator var7 =
        determinationInterval.entitledObjectives().iterator(); var7
          .hasNext(); indicativeAmount += tagOneTimeAmount) {
        final DeterminationIntervalObjective determinationIntervalObjective =
          (DeterminationIntervalObjective) var7.next();
        DeterminationIntervalTag oneTimeMonetaryTag = null;
        final Iterator var10 = determinationIntervalObjective
          .determinationIntervalTags().iterator();

        while (var10.hasNext()) {
          final DeterminationIntervalTag determinationIntervalTag =
            (DeterminationIntervalTag) var10.next();
          final TagType tagType = determinationIntervalTag.tagType();
          if (tagType.getValueType().equals(RULESTAGTYPEEntry.MONEY)) {
            final PatternType patternType =
              tagType.getPattern().getPatternType();
            if (patternType == PatternType.kDaily) {
              oneTimeMonetaryTag = determinationIntervalTag;
              break;
            }
          }
        }

        if (oneTimeMonetaryTag != null) {
          tagOneTimeAmount =
            Double.valueOf(oneTimeMonetaryTag.value().toString());
        } else {
          tagOneTimeAmount = 0.0D;
        }
      }

      // Create the summary message of the determination of how much the
      // individual is entitled for
      final Money indicativeAmountMoney = new Money(indicativeAmount);
      final AmountDetail amount = new AmountDetail();
      amount.amount = indicativeAmountMoney;
      final LocalisableString localisableString = new LocalisableString(
        BDMDETERMINATIONINTERVALSUMMARIZERS.INF_TOTAL_ONE_TIME_ENTITLEMENT)
          .arg(TabDetailFormatterFactory.newInstance()
            .formatCurrencyAmount(amount).amount);
      return localisableString.getMessage(TransactionInfo.getProgramLocale());
    }
  }
}
