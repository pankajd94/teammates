package teammates.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import teammates.common.datatransfer.AccountAttributes;
import teammates.common.datatransfer.InstructorAttributes;
import teammates.common.datatransfer.CourseAttributes;
import teammates.common.datatransfer.CourseDetailsBundle;
import teammates.common.util.Const;
import teammates.common.util.Sanitizer;
import teammates.ui.template.ActiveCoursesTable;
import teammates.ui.template.ActiveCoursesTableRow;
import teammates.ui.template.ArchivedCoursesTable;
import teammates.ui.template.ArchivedCoursesTableRow;
import teammates.ui.template.ElementTag;

/**
 * This is the PageData object for the 'Courses' page 
 */
public class InstructorCoursesPageData extends PageData {

    // Flag for deciding if loading the courses table, or the new course form.
    // if true -> loads the courses table, else load the form
    private boolean isUsingAjax;

    private ArchivedCoursesTable archivedCourses;
    private ActiveCoursesTable activeCourses;
    private String courseIdToShow;
    private String courseNameToShow;
    private Map<String, InstructorAttributes> instructorsForCourses;
    
    public InstructorCoursesPageData(AccountAttributes account) {
        super(account);
    }
    
    public void init(List<CourseAttributes> activeCoursesParam, List<CourseAttributes> archivedCoursesParam,
                     Map<String, InstructorAttributes> instructorsForCoursesParam){
        init(activeCoursesParam, archivedCoursesParam, instructorsForCoursesParam, "", ""); 
    }
    
    public void init(List<CourseAttributes> activeCoursesParam, List<CourseAttributes> archivedCoursesParam, 
                     Map<String, InstructorAttributes> instructorsForCoursesParam, String courseIdToShowParam,
                     String courseNameToShowParam) {
        this.instructorsForCourses = instructorsForCoursesParam;
        this.activeCourses = convertToActiveCoursesTable(activeCoursesParam);
        this.archivedCourses = convertToArchivedCoursesTable(archivedCoursesParam);
        this.courseIdToShow = courseIdToShowParam;
        this.courseNameToShow = courseNameToShowParam;
    }

    public void setUsingAjax(boolean isUsingAjax) {
        this.isUsingAjax = isUsingAjax;
    }

    public boolean isUsingAjax() {
      return this.isUsingAjax;
    }
    
    public String getCourseIdToShow() {
        return courseIdToShow;
    }
    
    public String getCourseNameToShow() {
        return courseNameToShow;
    }

    public ActiveCoursesTable getActiveCourses() {
        return activeCourses;
    }

    public ArchivedCoursesTable getArchivedCourses() {
        return archivedCourses;
    }
    
    private ArchivedCoursesTable convertToArchivedCoursesTable(List<CourseAttributes> archivedCourses) {
        ArchivedCoursesTable archivedCoursesTable = new ArchivedCoursesTable();
        
        int idx = this.activeCourses.getRows().size() - 1;
        
        for (CourseAttributes course : archivedCourses) {
            idx++;
            
            List<ElementTag> actionsParam = new ArrayList<ElementTag>();
            
            ElementTag unarchivedButton = createButton("Unarchive", "btn btn-default btn-xs", "t_course_unarchive" + idx,
                                                       getInstructorCourseArchiveLink(course.id, false, false), "", "", false);
            
            ElementTag deleteButton = createButton("Delete", "btn btn-default btn-xs", "t_course_delete" + idx,
                                                   getInstructorCourseDeleteLink(course.id, false), Const.Tooltips.COURSE_DELETE,
                                                   "return toggleDeleteCourseConfirmation('" + course.id + "');",
                                                   !instructorsForCourses.get(course.id).isAllowedForPrivilege(
                                                                                           Const.ParamsNames.INSTRUCTOR_PERMISSION_MODIFY_COURSE));
            
            actionsParam.add(unarchivedButton);
            actionsParam.add(deleteButton);
            
            ArchivedCoursesTableRow row = new ArchivedCoursesTableRow(Sanitizer.sanitizeForHtml(course.id), 
                                                                      Sanitizer.sanitizeForHtml(course.name), actionsParam);
            archivedCoursesTable.getRows().add(row);
            
        }
        
        return archivedCoursesTable;
    }
    
    private ActiveCoursesTable convertToActiveCoursesTable(List<CourseAttributes> courses) {
        ActiveCoursesTable activeCourses = new ActiveCoursesTable();
        
        int idx = -1;
        
        for (CourseAttributes course : courses) {
            idx++;
            
            List<ElementTag> actionsParam = new ArrayList<ElementTag>();
            
            ElementTag enrollButton = createButton("Enroll", "btn btn-default btn-xs t_course_enroll" + idx, "",
                                                   getInstructorCourseEnrollLink(course.id),
                                                   Const.Tooltips.COURSE_ENROLL, "", 
                                                   !instructorsForCourses.get(course.id).isAllowedForPrivilege(
                                                                                           Const.ParamsNames.INSTRUCTOR_PERMISSION_MODIFY_STUDENT));
            
            ElementTag viewButton = createButton("View", "btn btn-default btn-xs t_course_view" + idx, "",
                                                 getInstructorCourseDetailsLink(course.id), 
                                                 Const.Tooltips.COURSE_DETAILS, "", false);
            
            ElementTag editButton = createButton("Edit", "btn btn-default btn-xs t_course_edit" + idx, "",
                                                 getInstructorCourseEditLink(course.id), 
                                                 Const.Tooltips.COURSE_EDIT, "", false);
            
            ElementTag archiveButton = createButton("Archive", "btn btn-default btn-xs t_course_archive" + idx, "",
                                                    getInstructorCourseArchiveLink(course.id, true, false),
                                                    Const.Tooltips.COURSE_ARCHIVE, "", false);
            
            ElementTag deleteButton = createButton("Delete", "btn btn-default btn-xs t_course_delete" + idx, "",
                                                   getInstructorCourseDeleteLink(course.id, false),
                                                   Const.Tooltips.COURSE_DELETE, "return toggleDeleteCourseConfirmation('" + course.id + "');",
                                                   !(instructorsForCourses.get(course.id).isAllowedForPrivilege(
                                                                                           Const.ParamsNames.INSTRUCTOR_PERMISSION_MODIFY_COURSE)));
            
            actionsParam.add(enrollButton);
            actionsParam.add(viewButton);
            actionsParam.add(editButton);
            actionsParam.add(archiveButton);
            actionsParam.add(deleteButton);
            
            ActiveCoursesTableRow row = new ActiveCoursesTableRow(Sanitizer.sanitizeForHtml(course.id), 
                                                                  Sanitizer.sanitizeForHtml(course.name), 
                                                                  this.getInstructorCourseStatsLink(course.id),
                                                                  actionsParam);
            activeCourses.getRows().add(row);
        }
        
        return activeCourses;
    }
    
    private ElementTag createButton(String content, String buttonClass, String id, String href, String title, String onClick, boolean isDisabled){
        ElementTag button = new ElementTag(content);
        
        button.setAttribute("class", buttonClass);
        
        if ((id != null) && (!id.equals(""))) {
            button.setAttribute("id", id);
        }
        
        if ((href != null) && (!href.equals(""))) {
            button.setAttribute("href", href);
        }
        
        if ((title != null) && (!title.equals(""))) {
            button.setAttribute("title", title);
            button.setAttribute("data-toggle", "tooltip");
            button.setAttribute("data-placement", "top");
        }
        
        if ((onClick != null) && (!onClick.equals(""))) {
            button.setAttribute("onclick", onClick);
        }
        
        if (isDisabled) {
            button.setAttribute("disabled", null);
        }
        return button;
    }
}
