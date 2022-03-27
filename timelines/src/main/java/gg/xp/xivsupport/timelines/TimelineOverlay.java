package gg.xp.xivsupport.timelines;

import gg.xp.reevent.scan.ScanMe;
import gg.xp.xivsupport.gui.overlay.OverlayConfig;
import gg.xp.xivsupport.gui.overlay.OverlayMain;
import gg.xp.xivsupport.gui.overlay.RefreshType;
import gg.xp.xivsupport.timelines.gui.TimelineBarRenderer;
import gg.xp.xivsupport.gui.overlay.RefreshLoop;
import gg.xp.xivsupport.gui.overlay.XivOverlay;
import gg.xp.xivsupport.gui.tables.CustomColumn;
import gg.xp.xivsupport.gui.tables.CustomTableModel;
import gg.xp.xivsupport.persistence.PersistenceProvider;
import gg.xp.xivsupport.persistence.settings.IntSetting;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ScanMe
public class TimelineOverlay extends XivOverlay {
	private final TimelineManager timeline;
	private volatile List<VisualTimelineEntry> current = Collections.emptyList();
	private final IntSetting numberOfRows;
	private final CustomTableModel<VisualTimelineEntry> tableModel;
	private static final int BAR_WIDTH = 150;
	private final JTable table;

	public TimelineOverlay(PersistenceProvider persistence, TimelineManager timeline, OverlayConfig oc) {
		super("Timeline", "timeline-overlay", oc, persistence);
		// TODO: fix the timer getting truncated. Just left-justify text and right-justify timer like on cactbot
		this.timeline = timeline;
		this.numberOfRows = timeline.getRowsToDisplay();
		numberOfRows.addListener(this::repackSize);
		tableModel = CustomTableModel.builder(() -> current)
				.addColumn(new CustomColumn<>("Bar", Function.identity(),
						c -> {
							c.setCellRenderer(new TimelineBarRenderer());
							c.setMaxWidth(BAR_WIDTH);
							c.setMinWidth(BAR_WIDTH);
						}))
				.build();
		table = new JTable(tableModel);
		table.setOpaque(false);
		tableModel.configureColumns(table);
		table.setCellSelectionEnabled(false);
		getPanel().add(table);
		RefreshLoop<TimelineOverlay> refresher = new RefreshLoop<>("TimelineOverlay", this, TimelineOverlay::refresh, dt -> dt.calculateScaledFrameTime(200));
		repackSize();
		refresher.start();

	}

	@Override
	protected void repackSize() {
		table.setPreferredSize(new Dimension(table.getPreferredSize().width, table.getRowHeight() * numberOfRows.get()));
		super.repackSize();
	}

	private RefreshType getAndSort() {
		if (!getEnabled().get()) {
			current = Collections.emptyList();
			return RefreshType.NONE;
		}
		current = timeline.getCurrentDisplayEntries().stream().limit(numberOfRows.get()).collect(Collectors.toList());
		return RefreshType.FULL;
	}

	private void refresh() {
		RefreshType refreshTypeNeeded = getAndSort();
		switch (refreshTypeNeeded) {
			case FULL -> tableModel.fullRefresh();
			case REPAINT -> table.repaint();
		}
	}
}
