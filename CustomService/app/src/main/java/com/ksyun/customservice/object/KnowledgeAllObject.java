package com.ksyun.customservice.object;

import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */
public class KnowledgeAllObject {
    public List<KnowledgeKindObject> kinds;

    public List<KnowledgeKindObject> getKinds() {
        return kinds;
    }

    public void setKinds(List<KnowledgeKindObject> kinds) {
        this.kinds = kinds;
    }

    public static class KnowledgeKindObject {
        public String title;
        public List<KonwledgeObject> contentList;

        public List<KonwledgeObject> getContentList() {
            return contentList;
        }

        public void setContentList(List<KonwledgeObject> contentList) {
            this.contentList = contentList;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class KonwledgeObject {
        String question, answer;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

    }
}