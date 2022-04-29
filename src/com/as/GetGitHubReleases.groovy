package com.as

class GetGitHubReleases implements java.io.Serializable {
  def context

  GetGitHubReleases(context) {
    this.context = context
  }

  public void getReleases(String repoUrl){
    this.context.sh("echo Initial commit")
  }
}