package app.snack.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


data class QuestionsResponse(val data: List<Question>, val status: Int, val version: String) {

    companion object {
        fun mockProfileResponse() = QuestionsResponse(

            // GENERAL

            data = listOf(
                Question(
                    1,
                    questions = listOf("How does Snack work?", "Как работает Snack?"),
                    answers = listOf(
                        "Snack collects your unused traffic and sell it to large companies helping them to conduct analytical research. These findings allow businesses to make ad targeting more accurate, online shopping safer, e-commerce more competitive, and you to get a stable passive income.",
                        "Snack помогает пользователям заработать на неиспользованном интернет-соединении. Сервис сдает нереализованный трафик в аренду крупным компаниям для сбора информации в интернете и проведения внутренних исследований, а взамен гарантирует вознаграждение пользователю."
                    )
                ),
                Question(
                    2,
                    questions = listOf(
                        "How to get started?",
                        "Как начать зарабатывать?"
                    ),
                    answers = listOf(
                                "1. Download Snack app and sign up using your email and password.\n" +
                                "2. If necessary, verify your account by clicking on the link you'll get in a welcome letter.\n" +
                                "3. Set the volume of traffic you're ready to share with the app.\n" +
                                "4. Leave the app working on the background.\n",

                        "1. Скачайте приложение Snack и зарегистрируйтесь, используя адрес электронной почты и пароль.\n" +
                        "2. При необходимости подтвердите свою учетную запись, перейдя по ссылке, которую вы получите в письме.\n" +
                        "3. Установите объем трафика, которым вы готовы делиться.\n" +
                        "4. Оставьте приложение работающим в фоновом режиме.\n"
                    )
                ),
                Question(
                    3,
                    questions = listOf(
                        "How to make a payout?", "Как вывести заработанные средства?"
                    ),
                    answers = listOf(
                        "Once you've reached a \$10 threshold, you can request a payout by clicking on the corresponding button in your profile. You can withdraw funds from Snack to your credit/debit card.",
                        "Функция вывода средств становится доступной при достижении минимальной планки в \$10 на вашем внутреннем счете в Snack. Вы можете запросить вывод средств, кликнув на соответствующую кнопку в своем профиле, и перевести заработанные деньги на свою кредитную карту."
                    )
                ),
                Question(
                    4,
                    questions = listOf(
                        "How to stop sharing traffic?", "Как остановить сбор трафика?"
                    ),
                    answers = listOf(
                        "You can stop sharing traffic anytime you want. To do this, click on \"Stop sharing traffic\" button on your dashboard.",
                        "Для того, чтобы прекратить сбор трафика в фоновом режиме, нажмите соответствующую кнопку на панели мониторинга в своем аккаунте Snack."
                    )
                ),
                Question(
                    5,
                    questions = listOf("Is it secure?", "Безопасно ли использовать Snack?"),
                    answers = listOf(
                        "Absolutely. Business logic is transparent: Snack collects unused traffic and legally sells it only to trusted partners. Moreover, your privacy is also thoroughly protected, as the app does not access the users' browsing history or any activity records.",
                        "Да. Схема бизнеса прозрачна и не предполагает рисков: вы делитесь нереализованным интернет-соединением с компаниями и получаете деньги за избыточные данные. Кроме того, Snack гарантирует конфиденциальность: приложение не имеет доступа к истории поиска или данным о поведении пользователя в Интернете. "
                    )
                ),

                // PAYOUT CATEGORY

                Question(
                    6,
                    questions = listOf(
                        "What is the minimum balance threshold to withdraw money?",
                        "Существует ли минимальный баланс на счете Snack, по достижении которого я могу вывести средства?"
                    ),
                    answers = listOf(
                        "Yes. $10",
                        "Да. $10"
                    )
                ),
                Question(
                    7,
                    questions = listOf(
                        "Are there any withdrawal fees?",
                        "Взимается ли комиссия при выводе средств?"
                    ),
                    answers = listOf(
                        "No, Snack does not charge any fees.",
                        "Нет, вывод средств производится без комиссии"
                    )
                ),
                Question(
                    8,
                    questions = listOf(
                        "Why has my payout failed?",
                        "Почему мне не удается вывести средства?"
                    ),
                    answers = listOf(
                        "1. Make sure that you've reached a minimum earning threshold of $10 on your Snack balance account.\n" +
                        "2. Verify whether PayPal services are available in your country.",

                        "1. Убедитесь, что на вашем счете в Snack не менее \$10.\n" +
                        "2. Проверьте, доступны ли услуги PayPal в вашей стране."
                    )
                ),
                Question(
                    9,
                    questions = listOf(
                        "Are there any withdrawal deadlines?", "В течение какого времени необходимо вывести средства с внутреннего баланса Snack?"
                    ),
                    answers = listOf(
                        "No, there is no withdrawal deadlines. You can request a payout anytime once you’ve reached a 10\$ threshold. The money will be wired into your credit card account on 1st or 15th day of a month.",
                        "Ограничений по срокам вывода средств нет. Вы можете запросить вывод средств в любой момент, как только достигнете порога в 10\$ на внутреннем счете. Деньги поступят на счет вашей карты 1-го или 15-го числа месяца."
                    )
                ),

                // ACCOUNT

                Question(
                    10,
                    questions = listOf("I forgot my password. How to reset it?", "Забыл свой пароль. Как восстановить его?"),
                    answers = listOf(
                        "Click on \"Reset password\" in Snack app and follow instructions you will get on your email.",
                        "Используйте кнопку \"Сбросить пароль\" в приложении Snack и следуйте инструкциям, которые вы получите по электронной почте."
                    )
                ),
                Question(
                    11,
                    questions = listOf(
                        "How to verify/confirm my email address?", "Как подтвердить мой аккаунт?"
                    ),
                    answers = listOf(
                        "Click on \"Confirm email\" and follow the link inside an email you'll get.",
                        "Нажмите \"Подтвердить email\" и перейдите по ссылке из письма, которое вы получите."
                    )
                ),

                Question(
                    12,
                    questions = listOf(
                        "How to add a device to your Snack account?",
                        "Как добавить новое устройство?"
                    ),
                    answers = listOf(
                        "Download the app on a device and sign in — your device will be automatically chosen as a source of traffic.",
                        "Загрузите приложение и войдите в систему — ваше устройство будет автоматически выбрано в качестве источника трафика."
                    )
                ),

                Question(
                    13,
                    questions = listOf(
                        "How can I delete my account?", "Как удалить мой аккаунт в Snack?"
                    ),
                    answers = listOf(
                        "You can easily do this in your Snack account settings.",
                        "Вы можете удалить свой аккаунт в настройках учетной записи Snack."
                    )
                ),

                ),
            status = 200,
            version = "1.1"
        )
    }
}


data class Question(val id: Int, val questions: List<String>, val answers: List<String>) {

    val questionFromLocale
            get() = questions[if(Locale.getDefault().language == "ru") 1 else 0]

    val answerFromLocale
        get() = answers[if(Locale.getDefault().language == "ru") 1 else 0]

}