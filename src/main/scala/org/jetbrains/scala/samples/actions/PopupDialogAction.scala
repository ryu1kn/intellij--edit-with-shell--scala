// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.scala.samples.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.pom.Navigatable
import org.jetbrains.annotations.{Nls, NotNull, Nullable}
import org.jetbrains.scala.samples.SamplePluginBundle
import org.jetbrains.scala.samples.services.{ApplicationHelloService, ProjectHelloService}

import javax.swing._

class PopupDialogAction extends AnAction():

  /**
   * Gives the user feedback when the dynamic action menu is chosen.
   * Pops a simple message dialog.
   * @param event Event received when the associated menu item is chosen.
   */
  override def actionPerformed(event: AnActionEvent): Unit = // Using the event, create and show a dialog
    Messages.showMessageDialog(
      event.getProject,
      dialogMessage(event),
      event.getPresentation.getDescription,
      Messages.getInformationIcon
    )

  private def dialogMessage(event: AnActionEvent) =
    List(
      selectedText(event),
      navigationName(event),
      applicationHelloInfo,
      projectHelloInfo(event.getProject)
    ).filter(_ != null).mkString("\n")

  private val message = SamplePluginBundle.message(_: String, _: String)

  private def selectedText(event: AnActionEvent) =
    message("gettext.selected", event.getPresentation.getText)

  private def navigationName(event: AnActionEvent) =
    val nav = event.getData(CommonDataKeys.NAVIGATABLE)
    if nav != null then message("selected.element.tostring", nav.toString) else null

  private def projectHelloInfo(currentProject: Project) =
    ProjectHelloService.getInstance(currentProject).getProjectHelloInfo

  private def applicationHelloInfo =
    ApplicationHelloService.getInstance.getApplicationHelloInfo

  /**
   * Determines whether this menu item is available for the current context.
   * Requires a project to be open.
   *
   * @param e Event received when the associated group-id menu is chosen.
   */
  override def update(e: AnActionEvent): Unit = // Set the availability based on whether a project is open
    val project = e.getProject
    e.getPresentation.setEnabledAndVisible(project != null)
