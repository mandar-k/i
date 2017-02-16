package client.rootmodel

import com.livelygig.product.shared.models.ProjectsPost
import com.livelygig.product.shared.dtos._

case class ProjectsRootModel(projectsModelList: Seq[ProjectsPost]) {
  def updated(newProject: ProjectsPost): ProjectsRootModel = {
    projectsModelList.indexWhere(_.uid == newProject.uid) match {
      case -1 =>
        ProjectsRootModel(projectsModelList :+ newProject)
      case target =>
        ProjectsRootModel(projectsModelList.updated(target, newProject))
    }
  }
}
