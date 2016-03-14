package teammates.common.datatransfer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import teammates.common.util.Const;
import teammates.common.util.FeedbackQuestionFormTemplates;
import teammates.common.util.Sanitizer;
import teammates.ui.controller.PageData;
import teammates.ui.template.ElementTag;
import teammates.ui.template.InstructorFeedbackResultsResponseRow;

public class FeedbackRankRecipientsQuestionDetails extends FeedbackRankQuestionDetails {
    
    public FeedbackRankRecipientsQuestionDetails() {
        super(FeedbackQuestionType.RANK_RECIPIENTS);
    }

    public FeedbackRankRecipientsQuestionDetails(String questionText,
                                       List<String> rankOptions,
                                       int maxRank) {
        super(FeedbackQuestionType.RANK_RECIPIENTS, questionText);
    }

    
    @Override
    public boolean extractQuestionDetails(Map<String, String[]> requestParameters,
                                          FeedbackQuestionType questionType) {
        return super.extractQuestionDetails(requestParameters, questionType);
    }

    @Override
    public String getQuestionTypeDisplayName() {
        return Const.FeedbackQuestionTypeNames.RANK_RECIPIENT;
    }

    @Override
    public String getQuestionWithExistingResponseSubmissionFormHtml(
                        boolean sessionIsOpen, int qnIdx, int responseIdx, String courseId,
                        int totalNumRecipients,
                        FeedbackResponseDetails existingResponseDetails) {
        
        FeedbackRankRecipientsResponseDetails existingResponse = (FeedbackRankRecipientsResponseDetails) existingResponseDetails;
        StringBuilder optionListHtml = new StringBuilder();
        String optionFragmentTemplate = FeedbackQuestionFormTemplates.RANK_SUBMISSION_FORM_OPTIONFRAGMENT;
        
        String optionFragment = 
                FeedbackQuestionFormTemplates.populateTemplate(optionFragmentTemplate,
                        "${qnIdx}", Integer.toString(qnIdx),
                        "${responseIdx}", Integer.toString(responseIdx),
                        "${optionIdx}", "0",
                        "${disabled}", sessionIsOpen ? "" : "disabled",
                        "${rankOptionVisibility}", "style=\"display:none\"",
                        "${options}", getSubmissionOptionsHtmlForRankingRecipients(totalNumRecipients, existingResponse.answer),
                        "${Const.ParamsNames.FEEDBACK_RESPONSE_TEXT}", Const.ParamsNames.FEEDBACK_RESPONSE_TEXT,
                        "${rankOptionValue}", "");
        optionListHtml.append(optionFragment + Const.EOL);
        
        
        
        String html = FeedbackQuestionFormTemplates.populateTemplate(
                FeedbackQuestionFormTemplates.RANK_SUBMISSION_FORM,
                "${rankSubmissionFormOptionFragments}", optionListHtml.toString(),
                "${qnIdx}", Integer.toString(qnIdx),
                "${responseIdx}", Integer.toString(responseIdx),
                "${rankOptionVisibility}", "style=\"display:none\"" ,
                "${Const.ParamsNames.FEEDBACK_QUESTION_RANKTORECIPIENTS}", Const.ParamsNames.FEEDBACK_QUESTION_RANKTORECIPIENTS,
                "${rankToRecipientsValue}", "true",
                "${Const.ParamsNames.FEEDBACK_QUESTION_RANKNUMOPTION}", Const.ParamsNames.FEEDBACK_QUESTION_RANKNUMOPTIONS,
                "${rankNumOptionValue}", Integer.toString(0),
                "${Const.ParamsNames.FEEDBACK_QUESTION_RANKISDUPLICATESALLOWED}", Const.ParamsNames.FEEDBACK_QUESTION_RANKISDUPLICATESALLOWED,
                "${areDuplicatesAllowedValue}", Boolean.toString(areDuplicatesAllowed)
                );
        
        return html;
    }

    @Override
    public String getQuestionWithoutExistingResponseSubmissionFormHtml(
            boolean sessionIsOpen, int qnIdx, int responseIdx, String courseId, int totalNumRecipients) {
        
        StringBuilder optionListHtml = new StringBuilder();
        String optionFragmentTemplate = FeedbackQuestionFormTemplates.RANK_SUBMISSION_FORM_OPTIONFRAGMENT;
        
        String optionFragment = 
                FeedbackQuestionFormTemplates.populateTemplate(optionFragmentTemplate,
                        "${qnIdx}", Integer.toString(qnIdx),
                        "${responseIdx}", Integer.toString(responseIdx),
                        "${optionIdx}", "0",
                        "${disabled}", sessionIsOpen ? "" : "disabled",
                        "${rankOptionVisibility}", "style=\"display:none\"",
                        "${options}", getSubmissionOptionsHtmlForRankingRecipients(totalNumRecipients, Const.INT_UNINITIALIZED),
                        "${Const.ParamsNames.FEEDBACK_RESPONSE_TEXT}", Const.ParamsNames.FEEDBACK_RESPONSE_TEXT,
                        "${rankOptionValue}", "");
        optionListHtml.append(optionFragment + Const.EOL);
    
    
        String html = FeedbackQuestionFormTemplates.populateTemplate(
                            FeedbackQuestionFormTemplates.RANK_SUBMISSION_FORM,
                            "${rankSubmissionFormOptionFragments}", optionListHtml.toString(),
                            "${qnIdx}", Integer.toString(qnIdx),
                            "${responseIdx}", Integer.toString(responseIdx),
                            "${rankOptionVisibility}", "style=\"display:none\"",
                            "${rankToRecipientsValue}", "true",
                            "${Const.ParamsNames.FEEDBACK_QUESTION_RANKTORECIPIENTS}", Const.ParamsNames.FEEDBACK_QUESTION_RANKTORECIPIENTS,
                            "${Const.ParamsNames.FEEDBACK_QUESTION_RANKNUMOPTION}", Const.ParamsNames.FEEDBACK_QUESTION_RANKNUMOPTIONS,
                            "${rankNumOptionValue}", Integer.toString(0),
                            "${Const.ParamsNames.FEEDBACK_QUESTION_RANKISDUPLICATESALLOWED}", Const.ParamsNames.FEEDBACK_QUESTION_RANKISDUPLICATESALLOWED,
                            "${areDuplicatesAllowedValue}", Boolean.toString(areDuplicatesAllowed)
                            );
        
        return html;
    }
    
    
    private String getSubmissionOptionsHtmlForRankingRecipients(int totalNumRecipients, int rankGiven) {       
        
        StringBuilder result = new StringBuilder();
  
        ElementTag option = PageData.createOption("", "", rankGiven == Const.INT_UNINITIALIZED);
        result.append("<option" 
                     + option.getAttributesToString() + ">"
                     + option.getContent()
                     + "</option>");
        for (int i = 1; i <= totalNumRecipients; i++) {
            option = PageData.createOption(String.valueOf(i), String.valueOf(i), rankGiven == i);
            result.append("<option" 
                        + option.getAttributesToString() + ">"
                        + option.getContent()
                        + "</option>");
        }
        
       
        return result.toString();
    }

    @Override
    public String getQuestionSpecificEditFormHtml(int questionNumber) {
        
        return FeedbackQuestionFormTemplates.populateTemplate(
                FeedbackQuestionFormTemplates.RANK_EDIT_RECIPIENTS_FORM,
                "${questionNumber}", Integer.toString(questionNumber),
                "${optionRecipientDisplayName}", "recipient",
                "${Const.ParamsNames.FEEDBACK_QUESTION_RANKISDUPLICATESALLOWED}", Const.ParamsNames.FEEDBACK_QUESTION_RANKISDUPLICATESALLOWED,
                "${areDuplicatesAllowedChecked}", areDuplicatesAllowed ? "checked" : "");
    
    }

    @Override
    public String getNewQuestionSpecificEditFormHtml() {

        return "<div id=\"rankRecipientsForm\">" 
                + this.getQuestionSpecificEditFormHtml(-1) 
                + "</div>";
    }

    @Override
    public String getQuestionAdditionalInfoHtml(int questionNumber,
            String additionalInfoId) {
        String additionalInfo = this.getQuestionTypeDisplayName() + "<br>";
                
        String html = FeedbackQuestionFormTemplates.populateTemplate(
                        FeedbackQuestionFormTemplates.FEEDBACK_QUESTION_ADDITIONAL_INFO,
                        "${more}", "[more]",
                        "${less}", "[less]",
                        "${questionNumber}", Integer.toString(questionNumber),
                        "${additionalInfoId}", additionalInfoId,
                        "${questionAdditionalInfo}", additionalInfo);
        
        return html;
    }

    @Override
    public String getQuestionResultStatisticsHtml(
                        List<FeedbackResponseAttributes> responses,
                        FeedbackQuestionAttributes question,
                        String studentEmail,
                        FeedbackSessionResultsBundle bundle,
                        String view) {
        
        if (view.equals("student") || responses.isEmpty()) {
            return "";
        }
        
        String html = "";
        String fragments = "";
        
        Map<String, List<Integer>> recipientRanks = generateOptionRanksMapping(responses);

        DecimalFormat df = new DecimalFormat("#.##");
        
        for (Entry<String, List<Integer>> entry : recipientRanks.entrySet()) {
            
            List<Integer> ranks = entry.getValue();
            double average = computeAverage(ranks);
            String ranksReceived = getListOfRanksReceivedAsString(ranks);
            
            String participantIdentifier = entry.getKey();
            String name = bundle.getNameForEmail(participantIdentifier);
            String teamName = bundle.getTeamNameForEmail(participantIdentifier);
            
            fragments += FeedbackQuestionFormTemplates.populateTemplate(FeedbackQuestionFormTemplates.RANK_RESULT_STATS_RECIPIENTFRAGMENT,
                                                                        "${rankOptionValue}",  Sanitizer.sanitizeForHtml(name),
                                                                        "${team}", teamName,
                                                                        "${ranksReceived}", ranksReceived,
                                                                        "${averageRank}", df.format(average));

        }
     
        html = FeedbackQuestionFormTemplates.populateTemplate(FeedbackQuestionFormTemplates.RANK_RESULT_RECIPIENT_STATS,
                                                             "${optionRecipientDisplayName}", "Recipient",
                                                             "${fragments}", fragments);
        
        return html;
    }
    
    
    @Override
    public String getQuestionResultStatisticsCsv(
                        List<FeedbackResponseAttributes> responses,
                        FeedbackQuestionAttributes question,
                        FeedbackSessionResultsBundle bundle) {
        if (responses.isEmpty()) {
            return "";
        }
        
        String csv = "";
        String fragments = "";
        Map<String, List<Integer>> recipientRanks = generateOptionRanksMapping(responses);

        DecimalFormat df = new DecimalFormat("#.##");
        
        for (Entry<String, List<Integer>> entry : recipientRanks.entrySet()) {
            
            String teamName = bundle.getTeamNameForEmail(entry.getKey());
            String recipientName = bundle.getNameForEmail(entry.getKey());
            String option = Sanitizer.sanitizeForCsv(teamName) 
                            + "," 
                            + Sanitizer.sanitizeForCsv(recipientName);
           
            
            List<Integer> ranks = entry.getValue();
            double average = computeAverage(ranks);
            fragments += option + "," 
                         + df.format(average) + Const.EOL;
            
        }
        
        csv += "Team, Recipient" + ", Average Rank" + Const.EOL 
                + fragments + Const.EOL;
        
        return csv;
    }

    /**
     * From the feedback responses, generate a mapping of the option to a list of 
     * ranks received for that option.
     * The key of the map returned is the recipient's participant identifier.
     * The values of the map are list of ranks received by the recipient.   
     * @param responses  a list of responses 
     */
    private Map<String, List<Integer>> generateOptionRanksMapping(List<FeedbackResponseAttributes> responses) {
        
        Map<FeedbackResponseAttributes, Integer> normalisedRankOfResponse = getNormalisedRankForEachResponse(responses);

        Map<String, List<Integer>> optionRanks = new HashMap<>();
        for (FeedbackResponseAttributes response : responses) {
            updateOptionRanksMapping(optionRanks, response.recipientEmail, normalisedRankOfResponse.get(response));
        }
        
        return optionRanks;
    }

    /**
     * Returns a map of response to the normalised rank by resolving ties for each giver's set of responses
     * @param responses
     * @see FeedbackRankQuestionDetails#obtainMappingToNormalisedRanksForRanking(Map, List) for how ties are resolved
     */
    private Map<FeedbackResponseAttributes, Integer> getNormalisedRankForEachResponse(
                                                            List<FeedbackResponseAttributes> responses) {

        // collect each giver's responses
        Map<String, List<FeedbackResponseAttributes>> responsesGivenByPerson = new HashMap<>();
        for (FeedbackResponseAttributes response : responses) {
            if (!responsesGivenByPerson.containsKey(response.giverEmail)) {
                responsesGivenByPerson.put(response.giverEmail, new ArrayList<FeedbackResponseAttributes>());
            }
            
            responsesGivenByPerson.get(response.giverEmail)
                                  .add(response);
        }
        
        // resolve ties for each giver's responses
        Map<FeedbackResponseAttributes, Integer> normalisedRankOfResponse = new HashMap<>();
        for (Map.Entry<String, List<FeedbackResponseAttributes>> entry : responsesGivenByPerson.entrySet()) {
            Map<FeedbackResponseAttributes, Integer> rankOfResponse = new HashMap<>();
            for (FeedbackResponseAttributes res : responses) {
                FeedbackRankRecipientsResponseDetails frd = (FeedbackRankRecipientsResponseDetails)res.getResponseDetails();
                rankOfResponse.put(res, frd.answer);
            }
            
            normalisedRankOfResponse.putAll(obtainMappingToNormalisedRanksForRanking(rankOfResponse, entry.getValue()));
        }
        
        return normalisedRankOfResponse;
    }

    
    
    @Override
    public boolean isChangesRequiresResponseDeletion(FeedbackQuestionDetails newDetails) {
        return false;
    }
    

    @Override
    public String getCsvHeader() {
        return "Feedback";
    }

    @Override
    public String getQuestionTypeChoiceOption() {
        return "<option value=\"" + FeedbackQuestionType.RANK_RECIPIENTS.name() + "\">" 
              + Const.FeedbackQuestionTypeNames.RANK_RECIPIENT + "</option>";
    }

    @Override
    public List<String> validateQuestionDetails() {
        return new ArrayList<>();
    }

    @Override
    public List<String> validateResponseAttributes(
            List<FeedbackResponseAttributes> responses,
            int numRecipients) {
        if (responses.isEmpty()) {
            return new ArrayList<String>();
        }
        
        if (areDuplicatesAllowed) {
            return new ArrayList<String>();
        } else {
            List<String> errors = new ArrayList<>();
            
            Set<Integer> responseRank = new HashSet<>();
            for (FeedbackResponseAttributes response : responses) {
                FeedbackRankRecipientsResponseDetails frd = (FeedbackRankRecipientsResponseDetails) response.getResponseDetails();
                
                if (responseRank.contains(frd.answer)) {
                    errors.add("Duplicate rank " + frd.answer + " in question");
                } else if (frd.answer > numRecipients) {
                    errors.add("Invalid rank " + frd.answer + " in question");
                }
                responseRank.add(frd.answer);
            }
        
            return errors;
        }
    }
    
    @Override
    public Comparator<InstructorFeedbackResultsResponseRow> getResponseRowsSortOrder() {
        return new Comparator<InstructorFeedbackResultsResponseRow>() {

            @Override
            public int compare(InstructorFeedbackResultsResponseRow o1,
                               InstructorFeedbackResultsResponseRow o2) {
                
                if (!o1.getGiverTeam().equals(o2.getGiverTeam())) {
                    return o1.getGiverTeam().compareTo(o2.getGiverTeam());
                }
                
                if (!o1.getGiverDisplayableIdentifier().equals(o2.getGiverDisplayableIdentifier())) {
                    return o1.getGiverDisplayableIdentifier().compareTo(o2.getGiverDisplayableIdentifier());
                }
                
                if (!o1.getDisplayableResponse().equals(o2.getDisplayableResponse())) {
                    return o1.getDisplayableResponse().compareTo(o2.getDisplayableResponse());
                }
                
                if (!o1.getRecipientTeam().equals(o2.getRecipientTeam())) {
                    return o1.getRecipientTeam().compareTo(o2.getRecipientTeam());
                }
                
                return o1.getRecipientDisplayableIdentifier().compareTo(o2.getRecipientDisplayableIdentifier());
            }
            
        };
    }
}
