// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package io.ryuichi.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.{AnActionEvent, CommonDataKeys}
import com.intellij.openapi.command.WriteCommandAction

// Copied from https://plugins.jetbrains.com/docs/intellij/working-with-text.html
class ExecShellAction extends AnAction() {

  def actionPerformed(e: AnActionEvent): Unit = { // Get all the required data from data keys
    // Editor and Project were verified in update(), so they are not null.
    val editor = e.getRequiredData(CommonDataKeys.EDITOR)
    val project = e.getRequiredData(CommonDataKeys.PROJECT)
    val document = editor.getDocument
    // Work off of the primary caret to get the selection info
    val primaryCaret = editor.getCaretModel.getPrimaryCaret
    val start = primaryCaret.getSelectionStart
    val end = primaryCaret.getSelectionEnd
    // Replace the selection with a fixed string.
    // Must do this document change in a write action context.
    WriteCommandAction.runWriteCommandAction(project, (() => document.replaceString(start, end, "editor_basics")): Runnable)
    // De-select the text range that was just replaced
    primaryCaret.removeSelection()
  }

  override def update(e: AnActionEvent): Unit = { // Get required data keys
    val project = e.getProject
    val editor = e.getData(CommonDataKeys.EDITOR)
    // Set visibility and enable only in case of existing project and editor and if a selection exists
    e.getPresentation.setEnabledAndVisible(project != null && editor != null && editor.getSelectionModel.hasSelection)
  }
}
