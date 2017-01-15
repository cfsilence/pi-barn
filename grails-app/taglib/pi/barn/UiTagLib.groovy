package pi.barn

class UiTagLib {
    static namespace = "ui"

    /**
     * panel - a bootstrap panel
     * @panelClass the class used for the panel.  default=panel-primary
     * @panelId the id used for the panel.  default=uuid
     * @panelId the id used for the panel.  default=uuid
     * @showRefreshBtn should we show a refresh button in the panel header?  default=false
     * @title the text to display in the panel header
     */
    def panel = { attrs, body ->
        def pClass = attrs.panelClass ?: 'panel-primary'
        def showRefreshBtn = attrs.showRefreshBtn.toString() == 'true'
        def pId = attrs.panelId ?: UUID.randomUUID().toString().replaceAll('-', '')
        out << """
            <div class="panel ${pClass}" id="${pId}">
				<div class="panel-heading">
					<div class="pull-left">
					    <h3 class="panel-title">${attrs.title}</h3>
					</div>
					${showRefreshBtn ? """
                    <div class="pull-right">
					    <i class="fa fa-refresh pointer"></i>
					</div>
                    """ : ""}
					<div class="clearfix"></div>
				</div>

				<div class="panel-body">
					${body()}
				</div>
			</div>
        """
    }
}
