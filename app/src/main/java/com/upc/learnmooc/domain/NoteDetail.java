package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * Created by Explorer on 2016/4/9.
 */
public class NoteDetail {

	public ArrayList<NoteContent> noteDetail;

	public class NoteContent {
		public int noteId;
		public String time;
		public String content;


		@Override
		public String toString() {
			return "NoteContent{" +
					"noteId=" + noteId +
					", time='" + time + '\'' +
					", content='" + content + '\'' +
					'}';
		}

		public int getNoteId() {
			return noteId;
		}

		public void setNoteId(int noteId) {
			this.noteId = noteId;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getTime() {
			return time;
		}

		public String getContent() {
			return content;
		}
	}
}
