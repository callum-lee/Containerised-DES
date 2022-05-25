/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2020-2021 JaamSim Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaamsim.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * SearchField is a JTextField that performs auto-completion when searching for the entered string.
 * @author Harry King
 *
 */
public abstract class SearchField extends JPanel {

	private JTextField topicSearch;
	private ScrollablePopupMenu topicMenu;
	private final ArrayList<String> prevTopics = new ArrayList<>();  // previous topics viewed

	public SearchField(int columns) {
		setLayout( new FlowLayout(FlowLayout.CENTER, 0, 0) );

		// Topic to be searched
		topicSearch = new JTextField(columns);
		add(topicSearch);

		// Recent topics
		JButton dropdown = new BasicArrowButton(BasicArrowButton.SOUTH) {
	        @Override
			public Dimension getPreferredSize() {
	            return new Dimension(16, topicSearch.getPreferredSize().height);
	        }
		};
		dropdown.setToolTipText("Previous Searches");
		add(dropdown);

		// Dropdown button pressed
		dropdown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				topicMenu = new ScrollablePopupMenu();
				for (final String topic : prevTopics) {
					JMenuItem item = new JMenuItem(topic);
					item.setPreferredSize(topicSearch.getPreferredSize());
					item.addActionListener( new ActionListener() {

						@Override
						public void actionPerformed( ActionEvent event ) {
							topicMenu = null;
							showAndSaveTopic(topic);
						}
					} );
					topicMenu.add(item);
				}
				topicMenu.show(topicSearch, 0, topicSearch.getHeight());
			}
		});

		// Return pressed - pick the first topic that contains the string
		topicSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = topicSearch.getText().trim();
				ArrayList<String> topicList = getTopicList(str);
				if (topicList.isEmpty())
					return;
				showAndSaveTopic(topicList.get(0));
			}
		});

		// Down arrow pressed
		topicSearch.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_DOWN && topicMenu != null) {
					topicMenu.setVisible(false);
					String name = topicSearch.getText().trim();
					showTopicMenu(name, true);

					// Set the pop-up menu to the second item on the list
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							topicMenu.dispatchEvent(new KeyEvent(topicMenu, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_DOWN, '\0'));
						}
					});
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}
		});

		// Listen for changes to the text
		topicSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (e.getLength() != 1)  // single character entered
					return;
				String topic = topicSearch.getText().trim();
				showTopicMenu(topic, false);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String topic = topicSearch.getText().trim();
				showTopicMenu(topic, false);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
	    });

		// Focus on the search text
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				topicSearch.requestFocusInWindow();
			}
			@Override
			public void focusLost(FocusEvent e) {}
		});
	}

	@Override
	public void setToolTipText(String text) {
		topicSearch.setToolTipText(text);
	}

	public void setText(String str) {
		topicSearch.setText(str);
	}

	public String getText() {
		return topicSearch.getText();
	}

	public void search(String str) {
		topicSearch.setText(str);
		showTopicMenu(str, false);
		topicSearch.requestFocusInWindow();
	}

	private void showTopicMenu(String str, boolean focusable) {

		// Find the matching topics
		ArrayList<String> list = getTopicList(str);
		if (str.isEmpty() || list.isEmpty()) {
			if (topicMenu != null) {
				topicMenu.setVisible(false);
				topicMenu = null;
			}
			return;
		}

		// Build the menu of matching topics
		topicMenu = new ScrollablePopupMenu();
		boolean first = true;
		for (final String topic : list) {
			JMenuItem item = new JMenuItem(topic);
			item.setPreferredSize(topicSearch.getPreferredSize());
			item.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed( ActionEvent event ) {
					topicMenu = null;
					showAndSaveTopic(topic);
				}
			} );
			topicMenu.add(item);
			if (first && !focusable) {
				item.setArmed(true);
				first = false;
			}
		}
		if (!focusable)
			topicMenu.setFocusable(false);
		topicMenu.show(topicSearch, 0, topicSearch.getHeight());
	}

	public void showAndSaveTopic(String topic) {
		boolean bool = showTopic(topic);
		if (!bool)
			return;
		topicSearch.setText(topic);
		prevTopics.remove(topic);
		prevTopics.add(0, topic);
	}

	/**
	 * Returns the list of topics that contain the specified text.
	 * @param str - text to match with the topics
	 * @return list of matching topics
	 */
	public abstract ArrayList<String> getTopicList(String str);

	/**
	 * Displays the selected topic.
	 * @param topic - topic to display
	 * @return true if the topic was found
	 */
	public abstract boolean showTopic(String topic);

}
