package gg.xp.xivsupport.events.triggers.easytriggers.gui;

import gg.xp.xivsupport.events.triggers.easytriggers.EasyTriggers;
import gg.xp.xivsupport.events.triggers.easytriggers.model.AcceptsSaveCallback;
import gg.xp.xivsupport.events.triggers.easytriggers.model.Action;
import gg.xp.xivsupport.events.triggers.easytriggers.model.ActionDescription;
import gg.xp.xivsupport.events.triggers.easytriggers.model.HasMutableActions;
import gg.xp.xivsupport.gui.TitleBorderFullsizePanel;
import gg.xp.xivsupport.gui.library.ChooserDialog;
import gg.xp.xivsupport.gui.tables.CustomColumn;
import gg.xp.xivsupport.gui.tables.TableWithFilterAndDetails;
import gg.xp.xivsupport.gui.util.GuiUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

public class ActionsPanel<X> extends TitleBorderFullsizePanel {
	private static final Logger log = LoggerFactory.getLogger(ActionsPanel.class);
	private final HasMutableActions<X> trigger;
	private final Runnable saveCallback;
	private final EasyTriggers backend;
	private final ActionsPanelInner inner;

	public ActionsPanel(EasyTriggers backend, String label, HasMutableActions<X> trigger, Runnable saveCallback) {
		super(label);
		this.backend = backend;
		this.trigger = trigger;
		this.saveCallback = saveCallback;
		setPreferredSize(null);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		var buttonsArea = new JPanel();
		buttonsArea.setLayout(new GridBagLayout());
		GridBagConstraints c = GuiUtil.defaultGbc();
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.insets = new Insets(0, 2, 2, 3);
		buttonsArea.setAlignmentX(LEFT_ALIGNMENT);

		JButton newButton = EtGuiUtils.smallButton("New", this::addNewAction);
		buttonsArea.add(newButton, c);
		JButton pasteButton = EtGuiUtils.smallButton("Paste", this::tryPasteAction);
		c.gridx++;
		buttonsArea.add(pasteButton, c);
		c.gridx++;
		c.weightx = 1;
		buttonsArea.add(Box.createHorizontalGlue(), c);

		add(buttonsArea);

		add(Box.createHorizontalGlue());
		inner = new ActionsPanelInner();
		inner.initialize();
		add(inner);
		revalidate();
	}

	private void addNewAction() {
		TableWithFilterAndDetails<ActionDescription<?, ?>, Object> table = TableWithFilterAndDetails.builder(
						"Choose Action Type",
						() -> backend.getActionsApplicableTo(trigger))
				.addMainColumn(new CustomColumn<>("Action", c -> c.clazz().getSimpleName()))
				.addMainColumn(new CustomColumn<>("Description", ActionDescription::description))
				.setFixedData(true)
				.build();
		// TODO: owner
		ActionDescription<?, ?> desc = ChooserDialog.chooserReturnItem(SwingUtilities.getWindowAncestor(this), table);
		if (desc != null) {
			Action<?> newInst = desc.newInst();
			inner.add(newInst);
			revalidate();
			saveCallback.run();
		}
	}

	private class ActionsPanelInner extends JPanel implements DragDropTarget {

		private int previewIndex = -1;

		{
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setMaximumSize(new Dimension(32768, 32768));
//			setMinimumSize(new Dimension(50, 50));
		}

		@Override
		public Dimension getMinimumSize() {
			if (getComponentCount() == 0) {
//				return super.getMinimumSize();
				return new Dimension(200, 26);
			}
			else {
				return super.getMinimumSize();
			}
		}

		@Override
		public Dimension getPreferredSize() {
			if (getComponentCount() == 0) {
				return new Dimension(200, 26);
//				return super.getPreferredSize();
			}
			else {
				return super.getPreferredSize();
			}
		}

		@Override
		public Class<?> expectedClass() {
			return Action.class;
		}

		@Override
		public int indexFor(Point point) {
			Component[] components = getComponents();
			for (int i = 0; i < components.length; i++) {
				Component component = components[i];
				if (component.getY() + (component.getHeight() / 2) >= point.y) {
					return i;
				}
			}
			return components.length;
		}

		@Override
		public void previewDrop(Point point) {
			if (point == null) {
				previewIndex = -1;
			}
			else {
				previewIndex = indexFor(point);
			}
			repaint();
		}

		@Override
		public void doDrop(int index, Action<?> action) {
			addAt(action, index);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			int index = previewIndex;
			Component[] components = getComponents();
			if (index == 0) {
				g.setColor(Color.RED);
				g.fillRect(0, 0, this.getWidth(), 2);
			}
			else if (index >= 0 && index < components.length) {
				Component comp = components[index];
				g.setColor(Color.RED);
				g.fillRect(comp.getX(), comp.getY(), comp.getWidth(), 2);
			}
			else if (index == components.length) {
				// For inserting at the end, use the last component
				Component comp = components[components.length - 1];
				g.setColor(Color.RED);
				g.fillRect(comp.getX(), comp.getY() + comp.getHeight() - 2, comp.getWidth(), 2);
			}
		}

		public void initialize() {
			trigger.getActions().forEach(this::addPanel);
//			setBorder(new LineBorder(Color.PINK, 5));
		}

		public void add(Action<?> action) {
			trigger.addAction((Action<? super X>) action);
			addPanel(action);
		}

		public void addAt(Action<?> action, int index) {
			trigger.addAction((Action<? super X>) action, index);
			add(new ActionPanel<>(action), index);
			invalidate();
		}

		public void addPanel(Action<?> action) {
			add(new ActionPanel<>(action));
			invalidate();
		}

		public void remove(Action<?> action) {
			Component[] components = getComponents();
			for (int i = 0; i < components.length; i++) {
				Component component = components[i];
				if (component instanceof ActionsPanel<?>.ActionPanel<?> ap) {
					if (ap.action == action) {
						trigger.removeAction((Action<? super X>) action);
						remove(component);
						invalidate();
						SwingUtilities.invokeLater(this::revalidate);
						SwingUtilities.invokeLater(this::repaint);
						return;
					}
				}
			}
		}

		public void rearrange(Action<?> action, int newIndex) {
			List<Action<? super X>> actions = trigger.getActions();
			int oldIndex = actions.indexOf(action);
			if (oldIndex < 0) {
				log.error("Invalid index: {}", oldIndex);
			}
			else if (oldIndex == newIndex) {
				log.info("No-op DnD on index {}", newIndex);
			}
			else if (oldIndex < newIndex) {
				remove(action);
				// Subtract one to compensate for shifting indices
				addAt(action, newIndex - 1);
			}
			else {
				remove(action);
				addAt(action, newIndex);
			}
			invalidate();
			SwingUtilities.invokeLater(this::repaint);

		}
	}

	private class ActionPanel<Y> extends JPanel {

		private final Action<Y> action;

		ActionPanel(Action<Y> action) {
			this.action = action;
			setAlignmentX(LEFT_ALIGNMENT);
			setBorder(null);
			setLayout(new GridBagLayout());

			MouseAdapter dummyAdapter = new MouseAdapter() {

				//				private Component lastEnteredComponent;
				private DragDropTarget lastAddt;

				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (lastAddt != null) {
						lastAddt.previewDrop(null);
					}
					AddtPosition addtp = getAddt(e);
					// Actually do the drag
					if (addtp != null) {
						doDnd(addtp, action);
					}
					super.mouseReleased(e);
					SwingUtilities.invokeLater(inner::revalidate);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					super.mouseEntered(e);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					super.mouseExited(e);
				}

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					super.mouseWheelMoved(e);
				}

				@Override
				public void mouseDragged(MouseEvent e) {
//					log.info("Drag: {}", e.getComponent());
					AddtPosition addtp = getAddt(e);
					if (addtp != null) {
						if (lastAddt != addtp.addt && lastAddt != null) {
							lastAddt.previewDrop(null);
						}
						lastAddt = addtp.addt;
						addtp.addt.previewDrop(addtp.relativePoint);
					}
					else {
						if (lastAddt != null) {
							lastAddt.previewDrop(null);
							lastAddt = null;
						}
					}
				}

				@Override
				public void mouseMoved(MouseEvent e) {
//					log.info("Move: {}", e.getComponent());
					super.mouseMoved(e);
				}

				private @Nullable AddtPosition getAddt(MouseEvent e) {
					// TODO: this logic has to be re-done if splitting into multiple windows is implemented
//					log.info("Drag: {}", e.getComponent());
//					e.getComponent()
					// First, get the abs location of the event
					Point eventPoint = e.getLocationOnScreen();
					// Then, get the abs location of the window
					Window window = SwingUtilities.getWindowAncestor(e.getComponent());
					Point windowPoint = window.getLocationOnScreen();
					// Compute point within window
					Point deltaWithinWindow = new Point(eventPoint.x - windowPoint.x, eventPoint.y - windowPoint.y);
					// Then, find the component there
					Component c = SwingUtilities.getDeepestComponentAt(window, deltaWithinWindow.x, deltaWithinWindow.y);
					while (c != null) {
						if (c instanceof DragDropTarget addt && addt.expectedClass().isAssignableFrom(Action.class)) {
							Point compLos = c.getLocationOnScreen();
							Point relative = new Point(eventPoint.x - compLos.x, eventPoint.y - compLos.y);
							return new AddtPosition(addt, relative, addt.indexFor(relative));
						}
						c = c.getParent();
					}
					return null;
				}
			};

			GridBagConstraints c = GuiUtil.defaultGbc();
			c.anchor = GridBagConstraints.WEST;
			c.fill = GridBagConstraints.NONE;
			c.ipadx = 3;
			c.weightx = 0;

			{
				JLabel dragHandle = new JLabel("⣿");
				dragHandle.addMouseListener(dummyAdapter);
				dragHandle.addMouseMotionListener(dummyAdapter);
				dragHandle.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				add(dragHandle, c);
				c.gridx++;
			}

			c.insets = new Insets(2, 2, 2, 3);
			JButton deleteButton = EtGuiUtils.smallButton("Delete", this::delete);
			add(deleteButton, c);
			c.gridx++;
			JButton cutButton = EtGuiUtils.smallButton("Cut", this::cut);
			add(cutButton, c);
			c.gridx++;
			JButton copyButton = EtGuiUtils.smallButton("Copy", this::copy);
			add(copyButton, c);
			c.gridx++;

			String fixedLabel = action.fixedLabel();
			if (fixedLabel != null) {
				JLabel labelLabel = new JLabel(fixedLabel);
				add(labelLabel, c);
				c.gridx++;
			}
			c.weightx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			Component component;
			Class<? extends Action> actionClass = action.getClass();
			ActionDescription<Action<Y>, Y> desc = backend.getActionDescription(actionClass);
			try {
				if (desc == null) {
					component = new JLabel("Error: cannot find component");
				}
				else {
					component = desc.guiprovider().apply(action, trigger);
					if (component == null) {
						component = new JLabel("Error: null component");
					}
				}
			}
			catch (Throwable t) {
				log.error("Error making condition component", t);
				component = new JLabel("Error making component");
			}
			add(component, c);
			propSaveCallback(component);
			if (component instanceof AcceptsSaveCallback asc) {
				asc.setSaveCallback(saveCallback);
			}
//			c.weightx = 1;
//			add(Box.createHorizontalGlue(), c);
		}

		private void delete() {
			inner.remove(action);
		}

		private void copy() {
			GuiUtil.copyTextToClipboard(backend.exportAction(action));
		}

		private void cut() {
			copy();
			delete();
		}

		private void propSaveCallback(Component component) {
			if (component instanceof AcceptsSaveCallback asc) {
				asc.setSaveCallback(saveCallback);
			}
			if (component instanceof Container cont) {
				for (Component child : cont.getComponents()) {
					propSaveCallback(child);
				}
			}
		}
	}

	private void save() {
		saveCallback.run();
	}

	record AddtPosition(DragDropTarget addt, Point relativePoint, int index) {

	}

	private <Y> void doDnd(AddtPosition addtp, Action<Y> action) {
		DragDropTarget target = addtp.addt;
		if (target == this.inner) {
			inner.rearrange(action, addtp.index);
		}
		else {
			addtp.addt().doDrop(addtp.index, action);
			inner.remove(action);
		}
		save();
	}

	private void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void tryPasteAction() {
		String text = GuiUtil.getTextFromClipboard();
		if (text == null) {
			showError("No action on clipboard");
			return;
		}
		Action<?> action;
		try {
			action = backend.importAction(text);
		}
		catch (Exception e) {
			showError("No action on clipboard");
			return;
		}
		if (action == null) {
			showError("No action on clipboard");
			return;
		}
		Class<?> triggerType = trigger.getEventType();
		Class<?> conditionType = action.getEventType();
		if (!conditionType.isAssignableFrom(triggerType)) {
			showError("Action type " + action.getClass().getSimpleName() + " is not applicable to a trigger for " + triggerType.getSimpleName());
			return;
		}
		inner.add(action);
		SwingUtilities.invokeLater(this::revalidate);
		SwingUtilities.invokeLater(this::repaint);
		saveCallback.run();
	}

}
