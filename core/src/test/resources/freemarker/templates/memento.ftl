${root}
${root.id}
${root.type}
${root.getChildren()?size}
${root.getChild('section', 'daliConfig')}
${root.getChild('section', 'daliConfig').getChild('option','enablePushButtons').id}
${root.getChild('section', 'daliConfig').getChild('option','enablePushButtons').getString('selection')}

<#assign ps = root.getChild('section', 'daliPowerControl').getChild('option','A1')>
${ps.getString('selection')}
${ps.getChild('item',ps.getString('selection')).getString('input')}
